package com.bonjur.notification.data;

import com.bonjur.notification.domain.useCase.NotificationUseCase;
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
public final class UnreadCountProviderImpl_Factory implements Factory<UnreadCountProviderImpl> {
  private final Provider<NotificationUseCase> useCaseProvider;

  private UnreadCountProviderImpl_Factory(Provider<NotificationUseCase> useCaseProvider) {
    this.useCaseProvider = useCaseProvider;
  }

  @Override
  public UnreadCountProviderImpl get() {
    return newInstance(useCaseProvider.get());
  }

  public static UnreadCountProviderImpl_Factory create(
      Provider<NotificationUseCase> useCaseProvider) {
    return new UnreadCountProviderImpl_Factory(useCaseProvider);
  }

  public static UnreadCountProviderImpl newInstance(NotificationUseCase useCase) {
    return new UnreadCountProviderImpl(useCase);
  }
}
