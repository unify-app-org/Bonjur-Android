package com.bonjur.member.report;

import com.bonjur.network.report.ReportService;
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
public final class ReportViewModel_Factory implements Factory<ReportViewModel> {
  private final Provider<ReportService> serviceProvider;

  private ReportViewModel_Factory(Provider<ReportService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public ReportViewModel get() {
    return newInstance(serviceProvider.get());
  }

  public static ReportViewModel_Factory create(Provider<ReportService> serviceProvider) {
    return new ReportViewModel_Factory(serviceProvider);
  }

  public static ReportViewModel newInstance(ReportService service) {
    return new ReportViewModel(service);
  }
}
