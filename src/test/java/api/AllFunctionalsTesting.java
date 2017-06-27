package api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    ArticleResourceFunctionalTesting.class,
    CategoryResourceFunctionalTesting.class,
    DatabaseSeedResourceFunctionalTesting.class,
    EmbroideryResourceFunctionalTesting.class,
    InvoiceResourceFunctionalTesting.class,
    PdfGenerationResourceFunctionalTesting.class,
    ProductResourceFunctionalTesting.class, 
    TextilePrintingResourceFunctionalTesting.class,
    TicketResourceFunctionalTesting.class,
    TokenResourceFunctionalTesting.class,
    UserResourceFunctionalTesting.class,
    VoucherResourceFunctionalTesting.class
})

public class AllFunctionalsTesting {

}
