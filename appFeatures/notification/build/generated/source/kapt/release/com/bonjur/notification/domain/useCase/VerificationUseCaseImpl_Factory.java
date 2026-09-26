package com.bonjur.notification.domain.useCase;

import com.bonjur.notification.data.dataSource.NotificationDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class VerificationUseCaseImpl_Factory implements Factory<VerificationUseCaseImpl> {
  private final Provider<NotificationDataSource> dataSourceProvider;

  private VerificationUseCaseImpl_Factory(Provider<NotificationDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public VerificationUseCaseImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static VerificationUseCaseImpl_Factory create(
      Provider<NotificationDataSource> dataSourceProvider) {
    return new VerificationUseCaseImpl_Factory(dataSourceProvider);
  }

  public static VerificationUseCaseImpl newInstance(NotificationDataSource dataSource) {
    return new VerificationUseCaseImpl(dataSource);
  }
}
