package com.bonjur.notification.presentation.verification;

import com.bonjur.notification.domain.useCase.VerificationUseCase;
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
public final class VerificationViewModel_Factory implements Factory<VerificationViewModel> {
  private final Provider<VerificationUseCase> useCaseProvider;

  private VerificationViewModel_Factory(Provider<VerificationUseCase> useCaseProvider) {
    this.useCaseProvider = useCaseProvider;
  }

  @Override
  public VerificationViewModel get() {
    return newInstance(useCaseProvider.get());
  }

  public static VerificationViewModel_Factory create(
      Provider<VerificationUseCase> useCaseProvider) {
    return new VerificationViewModel_Factory(useCaseProvider);
  }

  public static VerificationViewModel newInstance(VerificationUseCase useCase) {
    return new VerificationViewModel(useCase);
  }
}
