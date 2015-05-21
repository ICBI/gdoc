--- DATABASE ALTERATIONS ON DEV, STAGE, PROD
---*********************************************

-- 2014 done in stage
ALTER TABLE 
   COMMON.DATA_SOURCE 
ADD 
   (
      is_precision_medicine CHAR(1) NULL,
      is_translational_research CHAR(1) NULL,
      is_population_genetics CHAR(1) NULL
   );

alter table COMMON.attribute_vocabulary modify (
TERM    VARCHAR2(2000 BYTE)
);
alter table COMMON.attribute_vocabulary modify (
TERM_MEANING   VARCHAR2(2000 BYTE)
);

alter table COMMON.HTARRAY_REPORTER modify (
GENE_SYMBOL VARCHAR2(25 BYTE)
);

-- Dec 10, 2014. Increasing size of DATA_SOURCE primary key column from 3 numbers to 6 numbers. Done on dev, stage, prod
ALTER TABLE COMMON.DATA_SOURCE MODIFY DATA_SOURCE_ID NUMBER(6,0);
ALTER TABLE COMMON.DATA_SOURCE_CONTENT MODIFY DATA_SOURCE_ID NUMBER(6,0);
ALTER TABLE COMMON.DATA_SOURCE_CONTACT MODIFY DATA_SOURCE_ID NUMBER(6,0);
ALTER TABLE COMMON.ID_MAPPING MODIFY DATA_SOURCE_ID NUMBER(6,0);

--May 21, 2015. Adding GEO IDs for CA_ARRAY_COLON and CA_ARRAY_LUNG
update COMMON.DATA_SOURCE set abstract='This is the NCI Colon Cancer Study from caArray. Samples were obtained from patients, including primary colon cancer, polyps, metastases, and matched normal mucosa (obtained from the margins of the resection). The RNA was extracted from tissue samples obtained from resections and hybridized to Affymetrix HG-U133A arrays. RNA expression data was also obtained for a few cell lines, although it has not been included in G-DOC Plus. <br> <br> To download the mapping file between G-DOC IDs and the CaArray IDs, click here: <a href="https://gdoc.georgetown.edu/mapping/CaArrayColon.xlsx">https://gdoc.georgetown.edu/mapping/CaArrayColon.xlsx</a><br><br>The cell line data can be obtained from GEO.<br>Link to GEO: <a href="http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=GSE68468">http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=GSE68468</a> <br> <br> Publications URLs: <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/19359472">http://www.ncbi.nlm.nih.gov/pubmed/19359472</a> <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/12801868">http://www.ncbi.nlm.nih.gov/pubmed/12801868</a> <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/17699774">http://www.ncbi.nlm.nih.gov/pubmed/17699774</a> <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/17044051">http://www.ncbi.nlm.nih.gov/pubmed/17044051</a> <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/16489013">http://www.ncbi.nlm.nih.gov/pubmed/16489013</a> <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/18483253">http://www.ncbi.nlm.nih.gov/pubmed/18483253</a> <br> <a href= "http://www.ncbi.nlm.nih.gov/pubmed/15722375">http://www.ncbi.nlm.nih.gov/pubmed/15722375</a>' where SHORT_NAME
='CA_ARRAY_COLON';

update COMMON.DATA_SOURCE set abstract='This is the NCI Directors Challenge Study: Survival Prediction in Lung Adenocarcinoma from caArray. It is a large, training, testing, multi-site, blinded validation study to characterize the performance of several prognostic models based on gene expression for 442 lung adenocarcinomas. The hypotheses proposed examined whether microarray measurements of gene expression either alone or combined with basic clinical covariates (stage, age, sex) could be used to predict overall survival in lung cancer subjects. Several models examined produced risk scores that substantially correlated with actual subject outcome. Most methods performed better with clinical data, supporting the combined use of clinical and molecular information when building prognostic models for early-stage lung cancer. This study also provides the largest available set of microarray data (on a Arrymetrix HG-U133A chip) with extensive pathological and clinical annotation for lung adenocarcinomas  <br> <br> To download the mapping file between G-DOC IDs and the CaArray IDs, click here: <a href="https://gdoc.georgetown.edu/mapping/CaArrayLung.xlsx">https://gdoc.georgetown.edu/mapping/CaArrayLung.xlsx</a><br><br> Link to GEO: <a href="http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=GSE68465">http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=GSE68465</a>  <br><br> Publication URL: <a href= "http://www.ncbi.nlm.nih.gov/pubmed/18641660">http://www.ncbi.nlm.nih.gov/pubmed/18641660</a>	' where SHORT_NAME = 'CA_ARRAY_LUNG';

