package net.stickycode.mockwire.junit4;

import org.junit.runners.model.Statement;

import net.stickycode.mockwire.MockwireContainer;

final class MockwireContainerLifecycleStatement
    extends Statement {

  private final MockwireContainer container;

  private final Statement wrappedStatement;

  MockwireContainerLifecycleStatement(MockwireContainer container, Statement block) {
    this.container = container;
    this.wrappedStatement = block;
  }

  @Override
  public void evaluate()
      throws Throwable {
    startup();
    try {
      wrappedStatement.evaluate();
    }
    finally {
      shutdown();
    }
  }

  private void shutdown()
      throws AssertionError {
    try {
      container.shutdown();
    }
    catch (Throwable t) {
      throw new AssertionError(t);
    }
  }

  private void startup()
      throws AssertionError {
    try {
      container.startup();
    }
    catch (Throwable t) {
      throw new AssertionError(t);
    }
  }
}