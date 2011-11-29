package org.chai.kevin;

import grails.plugin.spock.UnitSpec
import org.chai.kevin.util.Utils;

public class UtilSpec extends UnitSpec {

	def "test for strip html"(){
		when:
		String htmlString = "Vaccination<br>";
		String noHtmlString = "Vaccination";
		htmlString = Utils.stripHtml(htmlString, htmlString.length());
		
		then:
		htmlString.equals(noHtmlString);
		
		when:
		htmlString = "<p class=\"p1\">CHW Supervision</p>";
		noHtmlString = "CHW Supervision";
		htmlString = Utils.stripHtml(htmlString, htmlString.length());
		
		then:
		htmlString.equals(noHtmlString);
		
		when:
		htmlString = "<div>Accountant</div><div><br></div>";
		noHtmlString = "Accountant";
		htmlString = Utils.stripHtml(htmlString, htmlString.length());
		
		then:
		htmlString.equals(noHtmlString);
		
		when:
		htmlString = "Eye Problems&nbsp;(trauma, cataract, other)";
		noHtmlString = "Eye Problems (trauma, cataract, other)";
		htmlString = Utils.stripHtml(htmlString, htmlString.length());
		
		then:
		htmlString.equals(noHtmlString);
		
		when:
		htmlString = "Bone &amp; Joint Diseases";
		noHtmlString = "Bone & Joint Diseases";
		htmlString = Utils.stripHtml(htmlString, htmlString.length());
		
		then:
		htmlString.equals(noHtmlString);
		
		when:
		htmlString = "Children 14 Years<br>Old &amp; Younger";
		noHtmlString = "Children 14 Years Old & Younger";
		htmlString = Utils.stripHtml(htmlString, htmlString.length());
		
		then:
		htmlString.equals(noHtmlString);
	}
}
