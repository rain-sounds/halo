package com.hippo.journal.extension;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@GVK(group = "journal.hippo.com",
        version = "v1alpha1",
        kind = "JournalCategory",
        plural = "journalcategories",
        singular = "journalcategory")
public class JournalCategory extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Spec spec;

    @Data
    @Schema(name = "JournalCategorySpec")
    public static class Spec {

        @Schema(description = "期刊类别名称", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;

        @Schema(description = "期刊类别英文名", maxLength = 50)
        private String nameEn;

        @Schema(description = "排序，越小越靠前")
        private Integer sort;
    }
}
