package com.bonjur.notification.presentation.feed;

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
public final class NotificationFeedViewModel_Factory implements Factory<NotificationFeedViewModel> {
  private final Provider<NotificationUseCase> useCaseProvider;

  private NotificationFeedViewModel_Factory(Provider<NotificationUseCase> useCaseProvider) {
    this.useCaseProvider = useCaseProvider;
  }

  @Override
  public NotificationFeedViewModel get() {
    return newInstance(useCaseProvider.get());
  }

  public static NotificationFeedViewModel_Factory create(
      Provider<NotificationUseCase> useCaseProvider) {
    return new NotificationFeedViewModel_Factory(useCaseProvider);
  }

  public static NotificationFeedViewModel newInstance(NotificationUseCase useCase) {
    return new NotificationFeedViewModel(useCase);
  }
}
