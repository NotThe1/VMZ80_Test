package hardware;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdderAllRotates.class,
	AdderAllStandardTests.class,
	AdderBits.class })
public class AdderAllTests {

}
