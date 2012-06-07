package org.chai.kevin.reports;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.chai.kevin.Exportable;
import org.chai.kevin.Importable;
import org.chai.kevin.util.Utils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name="ReportProgram")
@Table(name="dhsst_report_program", uniqueConstraints={@UniqueConstraint(columnNames="code")})
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportProgram extends ReportEntity implements Exportable {

	private Long id;
	private ReportProgram parent;
	private List<ReportProgram> children = new ArrayList<ReportProgram>();
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity=ReportProgram.class)
	public ReportProgram getParent() {
		return parent;
	}
	
	public void setParent(ReportProgram parent) {
		this.parent = parent;
	}

	@OneToMany(targetEntity=ReportProgram.class, mappedBy="parent")
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public List<ReportProgram> getChildren() {
		return children;
	}
	
	public void setChildren(List<ReportProgram> children) {
		this.children = children;
	}
	
	public void addChild(ReportProgram child){
		children.add(child);
		child.setParent(this);
	}	

	@Override
	public String toExportString() {
		return "[" + Utils.formatExportCode(getCode()) + "]";
	}
	
//	@Override
//	public ReportProgram fromExportString(Object value) {
//		return (ReportProgram) value;
//	}
	
}
