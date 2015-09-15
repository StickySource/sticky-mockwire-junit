package net.stickycode.mockwire.junit4;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import net.stickycode.mockwire.MockwireContainer;

final class MockwireTestMethodLifecycleStatement
    extends Statement {

  private final MockwireContainer container;

  private final FrameworkMethod statement;

  private final Object test;

  MockwireTestMethodLifecycleStatement(MockwireContainer container, FrameworkMethod method, Object test) {
    this.container = container;
    this.statement = method;
    this.test = test;
  }

  @Override
  public void evaluate()
      throws Throwable {
    MockwireContainer testContainer = startTest();
    try {
      statement.invokeExplosively(test, testContainer.deriveParameters(statement.getMethod()));
    }
    finally {
      afterTest();
    }
  }

  private MockwireContainer startTest()
      throws AssertionError {
    try {
      return container.startTest(test);
    }
    catch (Throwable t) {
      throw new AssertionError(t);
    }
  }

  private void afterTest()
      throws AssertionError {
    try {
      container.endTest(test);
    }
    catch (Throwable t) {
      throw new AssertionError(t);
    }
  }

}