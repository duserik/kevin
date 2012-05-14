package org.chai.kevin.value;

import grails.plugin.springcache.annotations.CacheFlush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chai.kevin.Period;
import org.chai.kevin.data.Calculation;
import org.chai.kevin.data.DataElement;
import org.chai.kevin.data.DataService;
import org.chai.kevin.data.NormalizedDataElement;
import org.chai.kevin.location.CalculationLocation;
import org.chai.kevin.location.DataLocation;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class RefreshValueService {

	private final static Log log = LogFactory.getLog(RefreshValueService.class);
	
	private DataService dataService;
	private SessionFactory sessionFactory;
	private ExpressionService expressionService;
	private ValueService valueService;
	private GrailsApplication grailsApplication;
	
	@Transactional(readOnly = false)
	public void refreshNormalizedDataElement(NormalizedDataElement normalizedDataElement) {
		List<DataElement> dependencies = new ArrayList<DataElement>(); 
		collectOrderedDependencies(normalizedDataElement, dependencies);
		Collections.reverse(dependencies);
		for (DataElement dependency : dependencies) {
			if (dependency instanceof NormalizedDataElement) refreshNormalizedDataElementOnly((NormalizedDataElement)dependency);
		}
	}
	
	@Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
	public void refreshNormalizedDataElementOnly(NormalizedDataElement normalizedDataElement) {
		if (log.isDebugEnabled()) log.debug("refreshNormalizedDataElement(normalizedDataElement="+normalizedDataElement+")");
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.COMMIT);
		sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
		valueService.deleteValues(normalizedDataElement, null, null);
		for (Iterator<Object[]> iterator = getCombinations(DataLocation.class); iterator.hasNext();) {
			Object[] row = (Object[]) iterator.next();
			DataLocation dataLocation = (DataLocation)row[0];
			Period period = (Period)row[1];
			NormalizedDataElementValue value = expressionService.calculateValue(normalizedDataElement, dataLocation, period);				
			valueService.save(value);
		}
		normalizedDataElement.setCalculated(new Date());
		dataService.save(normalizedDataElement);
	}
	
	@Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
	public void refreshCalculationInTransaction(Calculation<?> calculation) {
		refreshCalculation(calculation);
	}
	
	@Transactional(readOnly = false)
	public void refreshCalculation(Calculation<?> calculation) {
		if (log.isDebugEnabled()) log.debug("refreshCalculation(calculation="+calculation+")");
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.COMMIT);
		sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
		
		valueService.deleteValues(calculation, null, null);
		for (Iterator<Object[]> iterator = getCombinations(CalculationLocation.class); iterator.hasNext();) {
			Object[] row = (Object[]) iterator.next();
			CalculationLocation location = (CalculationLocation)row[0];
			Period period = (Period)row[1];
			refreshCalculation(calculation, location, period);
		}
		calculation.setCalculated(new Date());
		dataService.save(calculation);
	}

	@CacheFlush(caches={"dsrCache", "dashboardCache", "fctCache"})
	public void flushCaches() { }
	
	@Transactional(readOnly = false)
	public void refreshNormalizedDataElements() {
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.COMMIT);
		sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
		
		// TODO get only those who need to be refreshed
		List<NormalizedDataElement> normalizedDataElements = sessionFactory.getCurrentSession().createCriteria(NormalizedDataElement.class).list();
 		
		while (!normalizedDataElements.isEmpty()) {
			NormalizedDataElement normalizedDataElement = normalizedDataElements.remove(0);
			if (normalizedDataElement.getCalculated() == null || normalizedDataElement.needsRefresh()) {
				List<DataElement> dependencies = new ArrayList<DataElement>();
				collectOrderedDependencies(normalizedDataElement, dependencies);
				Collections.reverse(dependencies);
				for (DataElement dependentElement : dependencies) {
					getMe().refreshNormalizedDataElementOnly((NormalizedDataElement)dependentElement);
					
					// we remove the element from the original list since it already has been updated
					normalizedDataElements.remove(dependentElement);
					sessionFactory.getCurrentSession().clear();
				}
			}
			normalizedDataElements.remove(normalizedDataElement);
		}
	}
	
	private void collectOrderedDependencies(DataElement dataElement, List<DataElement> dependencies) {
		dependencies.add(dataElement);
		if (dataElement instanceof NormalizedDataElement) {
			NormalizedDataElement normalizedDataElement = (NormalizedDataElement)dataElement;
			for (String expression : normalizedDataElement.getExpressions()) {
				Map<String, DataElement> dependenciesMap = expressionService.getDataInExpression(expression, DataElement.class);
				for (DataElement dependency : dependenciesMap.values()) {
					if (dependency != null && !dependencies.contains(dependency)) collectOrderedDependencies(dependency, dependencies);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void refreshCalculations() {
		sessionFactory.getCurrentSession().setFlushMode(FlushMode.COMMIT);
		sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
		
		// TODO get only those who need to be refreshed
		List<Calculation<?>> calculations = sessionFactory.getCurrentSession().createCriteria(Calculation.class).list();
		
		for (Calculation<?> calculation : calculations) {
			if (calculation.getCalculated() == null || calculation.needsRefresh()) {
				getMe().refreshCalculationInTransaction(calculation);
				sessionFactory.getCurrentSession().clear();
			}
		}
	}
	
	@Transactional(readOnly = true)
	public boolean needsUpdate(NormalizedDataElement normalizedDataElement, DataLocation location, Period period) {
		List<DataElement> dependencies = new ArrayList<DataElement>();
		collectOrderedDependencies(normalizedDataElement, dependencies);
		Collections.reverse(dependencies);
		for (DataElement dependency : dependencies) {
			StoredValue value = (StoredValue)valueService.getDataElementValue(dependency, location, period);
			if (value == null) return true;
			if (!value.getTimestamp().after(dependency.getTimestamp())) return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public void refreshCalculation(Calculation<?> calculation, CalculationLocation location, Period period) {
		valueService.deleteValues(calculation, location, period);
		for (CalculationPartialValue partialValue : expressionService.calculatePartialValues(calculation, location, period)) {
			valueService.save(partialValue);
		}
	}
	
	@Transactional(readOnly = false)
	public void refreshNormalizedDataElement(NormalizedDataElement dataElement, DataLocation dataLocation, Period period) {
		valueService.deleteValues(dataElement, dataLocation, period);
		List<DataElement> dependencies = new ArrayList<DataElement>(); 
		collectOrderedDependencies(dataElement, dependencies);
		Collections.reverse(dependencies);
		for (DataElement dependency : dependencies) {
			if (dependency instanceof NormalizedDataElement) valueService.save(expressionService.calculateValue((NormalizedDataElement)dependency, dataLocation, period));	
		}
	}

	private <T extends CalculationLocation> Iterator<Object[]> getCombinations(Class<T> clazz) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"select location, period " +
				"from "+clazz.getSimpleName()+" location, Period period"
		).setCacheable(true).setReadOnly(true);
		return query.iterate();
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void setExpressionService(ExpressionService expressionService) {
		this.expressionService = expressionService;
	}
	
	public void setValueService(ValueService valueService) {
		this.valueService = valueService;
	}
	
	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}
	
	public GrailsApplication getGrailsApplication() {
		return grailsApplication;
	}
	
	public void setGrailsApplication(GrailsApplication grailsApplication) {
		this.grailsApplication = grailsApplication;
	}
	
	public RefreshValueService getMe() {
		return grailsApplication.getMainContext().getBean(RefreshValueService.class);
	}
	
}
