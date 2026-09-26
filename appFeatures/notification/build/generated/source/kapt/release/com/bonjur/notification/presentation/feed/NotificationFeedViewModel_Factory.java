package com.bonjur.notification.presentation.feed;

import com.bonjur.notification.domain.useCase.NeedsActionUseCase;
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

  private final Provider<NeedsActionUseCase> needsActionUseCaseProvider;

  private NotificationFeedViewModel_Factory(Provider<NotificationUseCase> useCaseProvider,
      Provider<NeedsActionUseCase> needsActionUseCaseProvider) {
    this.useCaseProvider = useCaseProvider;
    this.needsActionUseCaseProvider = needsActionUseCaseProvider;
  }

  @Override
  public NotificationFeedViewModel get() {
    return newInstance(useCaseProvider.get(), needsActionUseCaseProvider.get());
  }

  public static NotificationFeedViewModel_Factory create(
      Provider<NotificationUseCase> useCaseProvider,
      Provider<NeedsActionUseCase> needsActionUseCaseProvider) {
    return new NotificationFeedViewModel_Factory(useCaseProvider, needsActionUseCaseProvider);
  }

  public static NotificationFeedViewModel newInstance(NotificationUseCase useCase,
      NeedsActionUseCase needsActionUseCase) {
    return new NotificationFeedViewModel(useCase, needsActionUseCase);
  }
}
