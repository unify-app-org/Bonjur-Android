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
public final class NotificationUseCaseImpl_Factory implements Factory<NotificationUseCaseImpl> {
  private final Provider<NotificationDataSource> dataSourceProvider;

  private NotificationUseCaseImpl_Factory(Provider<NotificationDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public NotificationUseCaseImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static NotificationUseCaseImpl_Factory create(
      Provider<NotificationDataSource> dataSourceProvider) {
    return new NotificationUseCaseImpl_Factory(dataSourceProvider);
  }

  public static NotificationUseCaseImpl newInstance(NotificationDataSource dataSource) {
    return new NotificationUseCaseImpl(dataSource);
  }
}
