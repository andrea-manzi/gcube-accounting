USE monitoring;
ALTER TABLE GHNMessage ADD INDEX GHNIndex(GHNName);
ALTER TABLE GHNMessage ADD INDEX TypeIndex(testType);
ALTER TABLE NOTIFICATION ADD INDEX TypeIndex(testType);
ALTER TABLE NOTIFICATION ADD INDEX SubTypeIndex(testSubType);