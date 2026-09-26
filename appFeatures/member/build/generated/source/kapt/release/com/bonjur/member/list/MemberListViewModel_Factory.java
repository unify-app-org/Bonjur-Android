package com.bonjur.member.list;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MemberListViewModel_Factory implements Factory<MemberListViewModel> {
  @Override
  public MemberListViewModel get() {
    return newInstance();
  }

  public static MemberListViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MemberListViewModel newInstance() {
    return new MemberListViewModel();
  }

  private static final class InstanceHolder {
    static final MemberListViewModel_Factory INSTANCE = new MemberListViewModel_Factory();
  }
}
