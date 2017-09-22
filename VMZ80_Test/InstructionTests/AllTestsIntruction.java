import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCB.class,
	            TestED.class,
	            TestDDandFD.class,
	            TestMainInstructions.class })
public class AllTestsIntruction {

}
