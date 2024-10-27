package org.vniizht.suburbsweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class SqlReplacingTest {

    @Autowired
    SalesReportJdbc salesReportJdbc;

    @Autowired
    DeparturesReportJdbc departuresReportJdbc;

    private final String commonCasesPath = "src/test/resources/sql-replacing-cases/";

//    @Test
//    void textSalesSqlCases() throws IOException {
//        String casesPath = commonCasesPath + "sales/";
//        for (int i = 1; i <= 5; i++) {
//            compareQueries(salesReportJdbc.getQueryByFieldValues(
//                    ResourcesAccess.getMapFromFile(new File(casesPath + "case-"+i+".json"))),
//                    ResourcesAccess.getFileContent(new File(casesPath + "case-"+i+".sql")));
//        }
//    }
//
//    @Test
//    void textDepartureSqlCases() throws IOException {
//        String casesPath = commonCasesPath + "departures/";
//        for (int i = 1; i <= 3; i++) {
//            compareQueries(departuresReportJdbc.getQueryByFieldValues(
//                            ResourcesAccess.getMapFromFile(new File(casesPath + "case-"+i+".json"))),
//                    ResourcesAccess.getFileContent(new File(casesPath + "case-"+i+".sql")));
//        }
//    }

    private void compareQueries(String testQuery, String properQuery){
        testQuery = removeSpaces(testQuery);
        properQuery = removeSpaces(properQuery);
        if(!testQuery.equals(properQuery))
            fail("\nincorrect:\t"+ testQuery +
                 "\nexpected: \t"+ properQuery);
    }

    private String removeSpaces(String target){
        return target.trim().replaceAll("\\s+", " ");
    }
}
