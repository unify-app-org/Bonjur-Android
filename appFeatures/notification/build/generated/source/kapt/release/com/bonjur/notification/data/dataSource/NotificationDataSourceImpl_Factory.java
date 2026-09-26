package com.bonjur.notification.data.dataSource;

import com.bonjur.network.APIClient.ApiClientProtocol;
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
public final class NotificationDataSourceImpl_Factory implements Factory<NotificationDataSourceImpl> {
  private final Provider<ApiClientProtocol> apiClientProvider;

  private NotificationDataSourceImpl_Factory(Provider<ApiClientProtocol> apiClientProvider) {
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public NotificationDataSourceImpl get() {
    return newInstance(apiClientProvider.get());
  }

  public static NotificationDataSourceImpl_Factory create(
      Provider<ApiClientProtocol> apiClientProvider) {
    return new NotificationDataSourceImpl_Factory(apiClientProvider);
  }

  public static NotificationDataSourceImpl newInstance(ApiClientProtocol apiClient) {
    return new NotificationDataSourceImpl(apiClient);
  }
}
