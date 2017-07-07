package hardware;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdderByteAdd.class,
	AdderByteSubtract.class,
	AdderWordAdd.class,
	AdderWordSubtract.class,
	AdderLogicalOperations.class })
public class AdderAllTests {

}
