<component name="libraryTable">
  <library name="Gradle: com.android.support:collections:28.0.0@jar">
    <CLASSES>
      <root url="jar://$USER_HOME$/.gradle/caches/modules-2/files-2.1/com.android.support/collections/28.0.0/c1bcdade4d3cc2836130424a3f3e4182c666a745/collections-28.0.0.jar!/" />
    </CLASSES>
    <JAVADOC />
    <SOURCES />
  </library>
</component>                                                                                                                                                                                                                                                                                                                                                                                                                     

#Glide图片加载框架
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#end Gilde图片加载框架