import gov.nih.nci.system.applicationservice.ApplicationService
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider
import gov.nih.nci.system.comm.client.ClientSession
import edu.wustl.catissuecore.domain.*
import gov.nih.nci.common.util.HQLCriteria;

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class CaTissueService {
	
	def sampleSummary() {
		System.setProperty("javax.net.ssl.trustStore","/Users/ashinohara/dev/georgetown/catissue_tests/chap8.keystore")

		def appService = ApplicationServiceProvider.getRemoteInstance()
		def cs = ClientSession.getInstance()
		cs.startSession("acs224@georgetown.edu", "cT2009")
		
		Specimen specimen = new Specimen();
		
		def resultList = appService.search(Specimen.class,specimen);
		def tissues = 0
		def molecular = 0
		def cell = 0
		def fluid = 0
		def sites = [:]
		resultList.each {
			println it.inspect()
			if(it instanceof TissueSpecimen) {
				println it.specimenCharacteristics.tissueSite
				tissues++
			} else if (it instanceof MolecularSpecimen) {
				molecular++
			} else if (it instanceof CellSpecimen) {
				cell++
			} else if (it instanceof FluidSpecimen) {
				fluid++
			}
			if(sites[it.specimenCharacteristics.tissueSite])
				sites[it.specimenCharacteristics.tissueSite] = 0
			sites[it.specimenCharacteristics.tissueSite]++
		}
		println sites
	}
}