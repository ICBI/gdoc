import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFDateUtil

class MoleculeService {

	boolean transactional = true

	def createMolecules(filePath) {
		Molecule.count()
		new ExcelBuilder(filePath).eachLine([labels:true]) {
			def molecule = new Molecule(
				smiles:smiles, 
				weight:weight,
				name:name,
				formula:formula,
			   	idnumber:idnumber,
				mCluster:m_cluster,
				clSize:cl_size,
				clVar:cl_var,
				available:available,
				clogP:clogp,
				donorAtoms:donor_atoms,
				acceptorAtoms:acceptor_atoms,
				rotatableBonds:rotatable_bonds,
				n_o:n_o,
				condrings:condrings,
				fosb:fosb,
				purity:purity,
				tpsa:tpsa,
				new_A:new_A,
				logsw:logsw,
				cns:cns,
				caco2:caco2,
				logbb:logbb,
				fa:fa,
				sw:sw,
				pka:pka,
				saltdata:saltdata)
				
				if (!molecule.save(flush: true)) {
					println molecule.errors
				}
				else {
					createStructure(molecule)
				}
		}
		return true
	}
	
	def createStructure(molecule) {
		def imgPath
		def structureFile
		def structure
		StructureFile.count()
		if(molecule.idnumber){
			imgPath = molecule.idnumber.replace(" ","_") + ".png"
		}else{
			imgPath = molecule.name.replace(" ","_") + ".png"
		}
		 structureFile = new StructureFile(name:molecule.name,relativePath:imgPath)
		if (!structureFile.save(flush: true)) {
			println structureFile.errors
		}else{
			Structure.count()
			structure = new Structure(structureFile:structureFile,molecule:molecule)
			if (!structure.save(flush: true)) {
				println structure.errors
			}
		}
		return structure
	}
	
}