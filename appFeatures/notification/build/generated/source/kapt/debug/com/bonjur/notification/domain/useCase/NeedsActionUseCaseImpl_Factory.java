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
public final class NeedsActionUseCaseImpl_Factory implements Factory<NeedsActionUseCaseImpl> {
  private final Provider<NotificationDataSource> dataSourceProvider;

  private NeedsActionUseCaseImpl_Factory(Provider<NotificationDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public NeedsActionUseCaseImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static NeedsActionUseCaseImpl_Factory create(
      Provider<NotificationDataSource> dataSourceProvider) {
    return new NeedsActionUseCaseImpl_Factory(dataSourceProvider);
  }

  public static NeedsActionUseCaseImpl newInstance(NotificationDataSource dataSource) {
    return new NeedsActionUseCaseImpl(dataSource);
  }
}
