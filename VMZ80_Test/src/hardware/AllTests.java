package hardware;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConditionCodeRegisterTest.class,
	            WorkingRegisterSetTest.class })
public class AllTests {

}
