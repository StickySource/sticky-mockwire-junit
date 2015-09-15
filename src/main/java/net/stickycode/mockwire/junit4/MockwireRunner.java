package net.stickycode.mockwire.junit4;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import net.stickycode.mockwire.Controlled;
import net.stickycode.mockwire.Mockwire;
import net.stickycode.mockwire.MockwireContainer;
import net.stickycode.mockwire.MockwireMetadata;
import net.stickycode.mockwire.MockwireMetadata;
import net.stickycode.mockwire.UnderTest;

/**
 * A jUnit runner to make your test classes and code behave like it would when run live in a di context ala Mockwire.
 *
 * The default context used for Dependency Injection is a manifest defined by the test class itself. It will only contain
 * {@link UnderTest code under test} and {@link Controlled controlled} classes in the actual test class or its super types.
 *
 * <pre>
 * package net.stickycode.example;
 *
 * &#064;RunWith(MockwireRunner.class)
 * public class ContainedTest {
 *
 *   &#064;UnderTest
 *   SomeConcreteClass field;
 *
 *   &#064;Inject
 *   SomeConcreteClass injectedInstanceOfBlessesClass;
 *
 *   &#064;Inject
 *   IsolateTestContext context;
 *
 *   &#064;Test
 *   public void testManifestHasCodeUnderTest() {
 *     assertThat(context.hasRegisteredType(ConcreteClass.class)).isTrue();
 *   }
 * }
 *
 * </pre>
 */
public class MockwireRunner
    extends BlockJUnit4ClassRunner {

  private MockwireContainer container;

  private final MockwireMetadata metadata;

  public MockwireRunner(Class<?> testClass)
      throws InitializationError {
    super(testClass);
    metadata = new MockwireMetadata(testClass);
    container = Mockwire.container(metadata);
  }


  @Override
  protected Statement classBlock(RunNotifier notifier) {
    if (!metadata.singleLifecycle())
      return super.classBlock(notifier);

    return new MockwireContainerLifecycleStatement(container, super.classBlock(notifier));
  }

  @Override
  protected Statement methodBlock(FrameworkMethod method) {
    if (metadata.singleLifecycle())
      return super.methodBlock(method);

    return new MockwireContainerLifecycleStatement(container, super.methodBlock(method));
  }


  @Override
  protected Statement methodInvoker(FrameworkMethod method, final Object test) {
    return new MockwireTestMethodLifecycleStatement(container, method, test);
  }
}
