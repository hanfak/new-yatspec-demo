package core.usecases;

import adapters.incoming.webserver.servlets.DataProvider;

import java.util.function.Supplier;

// Using a transient dependency is useful, in case the method we call on it is not thread safe
// If we used a singleton dependency, then multiple threads which access this usecase can cause non deterministic behaviour
// As it would access the non thread safe method at the same time or concurrently and cause problems
public class TransientDependencyUsecase {
  // Pass in a supplier which will instantiate the dependency when called get() on
  private final Supplier<DataProvider> dataProviderSupplier;

  public TransientDependencyUsecase(Supplier<DataProvider> dataProviderSupplier) {
    this.dataProviderSupplier = dataProviderSupplier;
  }

  public String execute() {
    // As the supplier has called get(), a new object is created
    // it sets the object reference to the local variable.
    DataProvider dataProvider = dataProviderSupplier.get();
    // For each call to this method a new instance of DataProvider will be created,
    // this will be seen in the different hash code for each call to this method
    System.out.println("dataProvider hashcode = " + dataProvider.hashCode());
    Integer personId = dataProvider.getPersonId("Chewbacca");
    return "Person Id of 'Chewbacca' is " + personId;
    // After the method has finished processing, the variable is removed off the stack
    // the transient dependency object reference is not being called by anyone
    // the garbage collector will now clean it up (at a time of it's choosing
  }
}
