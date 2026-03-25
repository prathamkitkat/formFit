package com.pm.formfitbackend.profile.service;

import com.pm.formfitbackend.exercise.entity.Exercise;
import com.pm.formfitbackend.exercise.repository.ExerciseRepository;
import com.pm.formfitbackend.profile.dto.*;
import com.pm.formfitbackend.workout.dto.WorkoutSummaryResponse;
import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutStatus;
import com.pm.formfitbackend.workout.mapper.WorkoutMapper;
import com.pm.formfitbackend.workout.repository.WorkoutRepository;
import com.pm.formfitbackend.workout.repository.WorkoutSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutSetRepository workoutSetRepository;
    private final ExerciseRepository exerciseRepository;

    public ProfileService(WorkoutRepository workoutRepository,
                          WorkoutSetRepository workoutSetRepository,
                          ExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutSetRepository = workoutSetRepository;
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Get main profile data (graphs for 1M + recent workouts)
     */
    public ProfileResponse getProfile(Long userId) {
        ProfileGraphsResponse graphs = getGraphs(userId, "1M");

        List<WorkoutSummaryResponse> recentWorkouts = workoutRepository
                .findByUserIdAndWorkoutStatusWithExercisesAndSetsOrderByStartedAtDesc(
                        userId,
                        WorkoutStatus.COMPLETED
                )
                .stream()
                .map(WorkoutMapper::toSummaryResponse)
                .collect(Collectors.toList());

        return new ProfileResponse(graphs, recentWorkouts);
    }

    /**
     * Get graph data for specified period
     */
    public ProfileGraphsResponse getGraphs(Long userId, String period) {
        // Calculate date range
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate;

        switch (period) {
            case "3M": startDate = endDate.minusMonths(3); break;
            case "6M": startDate = endDate.minusMonths(6); break;
            case "12M": startDate = endDate.minusMonths(12); break;
            case "1M":
            default: startDate = endDate.minusMonths(1); break;
        }

        // Get aggregated data from database (native query returns List<Object[]>)
        List<Object[]> aggregates = workoutRepository.getDailyGraphAggregatesNative(
                userId,
                startDate,
                endDate
        );

        // Convert Object[] to map for quick lookup
        // Object[]: [date, totalDuration, totalVolume, totalReps]
        Map<String, Object[]> aggregateMap = new HashMap<>();
        for (Object[] row : aggregates) {
            String date = row[0].toString();
            aggregateMap.put(date, row);
        }

        // Fill in missing days with 0 values
        List<GraphDataPoint> durationData = new ArrayList<>();
        List<GraphDataPoint> volumeData = new ArrayList<>();
        List<GraphDataPoint> repsData = new ArrayList<>();

        LocalDate current = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        while (!current.isAfter(end)) {
            String dateStr = current.toString();
            Object[] row = aggregateMap.get(dateStr);

            if (row != null) {
                // row[1] = totalDuration, row[2] = totalVolume, row[3] = totalReps
                Double duration = ((Number) row[1]).doubleValue();
                Double volume = ((Number) row[2]).doubleValue();
                Double reps = ((Number) row[3]).doubleValue();

                durationData.add(new GraphDataPoint(dateStr, duration));
                volumeData.add(new GraphDataPoint(dateStr, volume));
                repsData.add(new GraphDataPoint(dateStr, reps));
            } else {
                // No workout on this day
                durationData.add(new GraphDataPoint(dateStr, 0.0));
                volumeData.add(new GraphDataPoint(dateStr, 0.0));
                repsData.add(new GraphDataPoint(dateStr, 0.0));
            }

            current = current.plusDays(1);
        }

        return new ProfileGraphsResponse(durationData, volumeData, repsData);
    }

    /**
     * Get monthly report for specified month
     */
    public MonthlyReportResponse getMonthlyReport(Long userId, Integer year, Integer month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        // Convert year/month to date range for current month
        YearMonth currentYearMonth = YearMonth.of(year, month);
        LocalDateTime currentStartDate = currentYearMonth.atDay(1).atStartOfDay();
        LocalDateTime currentEndDate = currentYearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Get current month stats (native query returns Object[])
        // Object[]: [workoutCount, totalDuration, totalVolume, totalSets]
        List<Object[]> currentStatsRaw = workoutRepository.getMonthlyStatsNative(
                userId,
                currentStartDate,
                currentEndDate
        );
        Object[] currentStats = currentStatsRaw.get(0);

        // Get previous month date range
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        LocalDateTime previousStartDate = previousYearMonth.atDay(1).atStartOfDay();
        LocalDateTime previousEndDate = previousYearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Get previous month stats (native query returns Object[])
        List<Object[]> previousStatsRaw = workoutRepository.getMonthlyStatsNative(
                userId,
                previousStartDate,
                previousEndDate
        );
        Object[] previousStats = previousStatsRaw.get(0);

        // Extract current month values
        Long currentWorkouts = ((Number) currentStats[0]).longValue();
        Integer currentDuration = ((Number) currentStats[1]).intValue();
        Double currentVolume = ((Number) currentStats[2]).doubleValue();
        Long currentSets = ((Number) currentStats[3]).longValue();

        // Extract previous month values
        Long prevWorkouts = ((Number) previousStats[0]).longValue();
        Integer prevDuration = ((Number) previousStats[1]).intValue();
        Double prevVolume = ((Number) previousStats[2]).doubleValue();
        Long prevSets = ((Number) previousStats[3]).longValue();

        // Calculate comparison
        ComparisonData comparison = new ComparisonData(
                (int)(currentWorkouts - prevWorkouts),
                currentDuration - prevDuration,
                currentVolume - prevVolume,
                (int)(currentSets - prevSets)
        );

        // Build response
        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setYear(year);
        response.setMonth(month);
        response.setWorkouts(currentWorkouts.intValue());
        response.setTotalDurationMinutes(currentDuration);
        response.setTotalVolume(currentVolume);
        response.setTotalSets(currentSets.intValue());
        response.setComparison(comparison);

        return response;
    }

    /**
     * Get most frequently performed exercises
     */
    public List<FrequentExerciseResponse> getFrequentExercises(Long userId, Integer days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        // Native query returns List<Object[]>
        // Object[]: [id, name, imageUrl, timesPerformed]
        List<Object[]> results = workoutRepository.getFrequentExercisesNative(userId, startDate);

        return results.stream()
                .map(row -> new FrequentExerciseResponse(
                        ((Number) row[0]).longValue(),      // id
                        (String) row[1],                     // name
                        (String) row[2],                     // imageUrl
                        ((Number) row[3]).intValue()         // timesPerformed
                ))
                .collect(Collectors.toList());
    }

    /**
     * Get all exercises user has performed
     */
    public List<UserExerciseResponse> getUserExercises(Long userId) {
        List<Exercise> exercises = workoutRepository.getUserPerformedExercises(userId);

        return exercises.stream()
                .map(e -> new UserExerciseResponse(e.getId(), e.getName(), e.getImageUrl()))
                .collect(Collectors.toList());
    }

    /**
     * Get detailed stats for a specific exercise
     */
    public ExerciseStatsResponse getExerciseStats(Long userId, Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        ExerciseStatsResponse response = new ExerciseStatsResponse();
        response.setExerciseId(exerciseId);
        response.setExerciseName(exercise.getName());

        // Get heaviest weight (returns Object[] or null)
        // Object[]: [weight, endedAt]
        Object[] heaviestWeight = workoutSetRepository.getHeaviestWeight(userId, exerciseId);
        if (heaviestWeight != null && heaviestWeight.length > 0 && heaviestWeight[0] != null) {
            response.setHeaviestWeight(((Number) heaviestWeight[0]).doubleValue());
            response.setHeaviestWeightDate(heaviestWeight[1].toString());
        }

        // Get best 1RM
        // Object[]: [oneRM, endedAt]
        Object[] best1RM = workoutSetRepository.getBest1RM(userId, exerciseId);
        if (best1RM != null && best1RM.length > 0 && best1RM[0] != null) {
            response.setBest1RM(((Number) best1RM[0]).doubleValue());
            response.setBest1RMDate(best1RM[1].toString());
        }

        // Get best set volume
        // Object[]: [volume, endedAt]
        Object[] bestSetVolume = workoutSetRepository.getBestSetVolume(userId, exerciseId);
        if (bestSetVolume != null && bestSetVolume.length > 0 && bestSetVolume[0] != null) {
            response.setBestSetVolume(((Number) bestSetVolume[0]).doubleValue());
            response.setBestSetVolumeDate(bestSetVolume[1].toString());
        }

        // Get best session volume
        // Object[]: [sessionVolume, endedAt]
        Object[] bestSessionVolume = workoutSetRepository.getBestSessionVolume(userId, exerciseId);
        if (bestSessionVolume != null && bestSessionVolume.length > 0 && bestSessionVolume[0] != null) {
            response.setBestSessionVolume(((Number) bestSessionVolume[0]).doubleValue());
            response.setBestSessionVolumeDate(bestSessionVolume[1].toString());
        }

        return response;
    }

    /**
     * Get calendar data for a specific month
     */
    public List<CalendarDayResponse> getCalendar(Long userId, Integer year, Integer month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        // Convert year/month to date range
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Get all workouts for the month
        List<Workout> workouts = workoutRepository.getWorkoutsForCalendar(userId, startDate, endDate);

        // Group by date
        Map<LocalDate, List<Workout>> workoutsByDate = workouts.stream()
                .collect(Collectors.groupingBy(w -> w.getEndedAt().toLocalDate()));

        // Generate all days in the month
        List<CalendarDayResponse> calendar = new ArrayList<>();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            List<Workout> dayWorkouts = workoutsByDate.getOrDefault(date, Collections.emptyList());

            List<Long> workoutIds = dayWorkouts.stream()
                    .map(Workout::getId)
                    .collect(Collectors.toList());

            calendar.add(new CalendarDayResponse(
                    date.toString(),
                    dayWorkouts.size(),
                    workoutIds
            ));
        }

        return calendar;
    }
}