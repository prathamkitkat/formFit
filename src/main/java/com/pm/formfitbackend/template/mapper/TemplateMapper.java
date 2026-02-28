package com.pm.formfitbackend.template.mapper;

import com.pm.formfitbackend.template.dto.*;
import com.pm.formfitbackend.template.entity.Template;
import com.pm.formfitbackend.template.entity.TemplateExercise;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateMapper {

    /* ================= DETAIL ================= */

    public static TemplateDetailResponse toDetailResponse(Template template) {

        TemplateDetailResponse response = new TemplateDetailResponse();

        response.setTemplateId(template.getId());
        response.setName(template.getName());

        List<TemplateExerciseDetailResponse> exerciseResponses =
                template.getExercises().stream()
                        .sorted(Comparator.comparing(TemplateExercise::getOrderIndex))
                        .map(te -> {
                            TemplateExerciseDetailResponse r = new TemplateExerciseDetailResponse();
                            r.setTemplateExerciseId(te.getId());
                            r.setExerciseId(te.getExercise().getId());
                            r.setExerciseName(te.getExercise().getName());
                            r.setOrderIndex(te.getOrderIndex());
                            r.setDefaultSetCount(te.getDefaultSetCount());
                            return r;
                        })
                        .collect(Collectors.toList());

        response.setExercises(exerciseResponses);

        return response;
    }

    /* ================= SUMMARY ================= */

    public static TemplateSummaryResponse toSummaryResponse(Template template) {

        TemplateSummaryResponse response = new TemplateSummaryResponse();

        response.setTemplateId(template.getId());
        response.setName(template.getName());

        List<String> exerciseNames =
                template.getExercises().stream()
                        .map(te -> te.getExercise().getName())
                        .collect(Collectors.toList());

        response.setExerciseNames(exerciseNames);

        return response;
    }
}