package com.bonjur.notification.presentation.needsAction;

import com.bonjur.notification.domain.useCase.NeedsActionUseCase;
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
public final class NeedsActionViewModel_Factory implements Factory<NeedsActionViewModel> {
  private final Provider<NeedsActionUseCase> useCaseProvider;

  private NeedsActionViewModel_Factory(Provider<NeedsActionUseCase> useCaseProvider) {
    this.useCaseProvider = useCaseProvider;
  }

  @Override
  public NeedsActionViewModel get() {
    return newInstance(useCaseProvider.get());
  }

  public static NeedsActionViewModel_Factory create(Provider<NeedsActionUseCase> useCaseProvider) {
    return new NeedsActionViewModel_Factory(useCaseProvider);
  }

  public static NeedsActionViewModel newInstance(NeedsActionUseCase useCase) {
    return new NeedsActionViewModel(useCase);
  }
}
