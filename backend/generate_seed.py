import sys

muscle_groups = [
    "Abdominals", "Abductor", "Biceps", "Calves", "Cardio",
    "Chest", "Forearms", "Full Body", "Glutes", "Hamstrings",
    "Lats", "Lower Back", "Quadriceps", "Shoulders", "Traps",
    "Triceps", "Upper Back"
]

exercises_data = [
    # Abdominals
    "Crunch|Bodyweight", "Sit-up|Bodyweight", "Plank|Bodyweight", "Cable Crunch|Cable", "Hanging Leg Raise|Bodyweight", "Bicycle Crunch|Bodyweight", "Ab Wheel Rollout|Ab Wheel", "Russian Twist|Bodyweight", "V-up|Bodyweight", "Decline Crunch|Bench", "Machine Crunch|Machine", "Reverse Crunch|Bodyweight", "Dragon Flag|Bodyweight", "Flutter Kicks|Bodyweight", "Leg Raise|Bodyweight", "Toe Touches|Bodyweight", "Hollow Body Hold|Bodyweight", "Windshield Wipers|Bodyweight", "Cable Woodchopper|Cable", "Weighted Crunch|Dumbbell",
    # Abductor
    "Seated Hip Abduction Machine|Machine", "Cable Hip Abduction|Cable", "Band Hip Abduction|Band", "Lying Lateral Leg Raise|Bodyweight", "Clamshell|Bodyweight", "Banded Clamshell|Band", "Standing Leg Abduction|Bodyweight", "Side Plank with Leg Raise|Bodyweight", "Fire Hydrant|Bodyweight", "Banded Fire Hydrant|Band", "Dumbbell Lateral Lunge|Dumbbell", "Monster Walk|Band", "Lateral Band Walk|Band", "Curtsy Lunge|Bodyweight", "Barbell Curtsy Lunge|Barbell", "Skater Squat|Bodyweight", "Cossack Squat|Bodyweight", "Kettlebell Cossack Squat|Kettlebell", "Lateral Step-Up|Box", "Weighted Lateral Step-Up|Dumbbell",
    # Biceps
    "Barbell Curl|Barbell", "Dumbbell Curl|Dumbbell", "Hammer Curl|Dumbbell", "Preacher Curl|EZ Bar", "Cable Curl|Cable", "Concentration Curl|Dumbbell", "Incline Dumbbell Curl|Dumbbell", "Reverse Curl|Barbell", "Zottman Curl|Dumbbell", "Spider Curl|Dumbbell", "Machine Bicep Curl|Machine", "High Cable Curl|Cable", "EZ Bar Curl|EZ Bar", "Drag Curl|Barbell", "Plate Curl|Weight Plate", "Cross-body Hammer Curl|Dumbbell", "Waiter Curl|Dumbbell", "Cheat Curl|Barbell", "Rope Cable Curl|Cable", "Band Bicep Curl|Band",
    # Calves
    "Standing Calf Raise|Machine", "Seated Calf Raise|Machine", "Donkey Calf Raise|Machine", "Leg Press Calf Raise|Machine", "Smith Machine Calf Raise|Smith Machine", "Dumbbell Calf Raise|Dumbbell", "Single Leg Calf Raise|Bodyweight", "Barbell Calf Raise|Barbell", "Farmers Walk on Toes|Dumbbell", "Jump Rope|Rope", "Box Jumps|Box", "Calf Press on Leg Press|Machine", "Elevated Calf Raise|Bodyweight", "Tibialis Raise|Bodyweight", "Banded Calf Raise|Band", "Squat Hold Calf Raise|Bodyweight", "Agility Ladder Drills|Ladder", "Sled Push|Sled", "Ankle Hops|Bodyweight", "Walking Calf Raise|Bodyweight",
    # Cardio
    "Running|Treadmill", "Cycling|Stationary Bike", "Rowing|Rowing Machine", "Elliptical|Elliptical", "Stair Climber|Stair Machine", "SkiErg|Machine", "Assault Bike|Bike", "Swimming|None", "Burpees|Bodyweight", "Jumping Jacks|Bodyweight", "Mountain Climbers|Bodyweight", "High Knees|Bodyweight", "Sprints|Bodyweight", "Battle Ropes|Ropes", "Kettlebell Swings|Kettlebell", "Boxing|Heavy Bag", "Aerobics|None", "Zumba|None", "Jump Squats|Bodyweight", "Bear Crawls|Bodyweight",
    # Chest
    "Barbell Bench Press|Barbell", "Dumbbell Bench Press|Dumbbell", "Incline Barbell Bench Press|Barbell", "Incline Dumbbell Bench Press|Dumbbell", "Decline Barbell Bench Press|Barbell", "Decline Dumbbell Bench Press|Dumbbell", "Push-up|Bodyweight", "Dumbbell Flyes|Dumbbell", "Cable Crossover|Cable", "Pec Deck Machine|Machine", "Chest Dip|Bodyweight", "Svend Press|Plate", "Floor Press|Barbell", "Hex Press|Dumbbell", "Guillotine Press|Barbell", "Machine Chest Press|Machine", "Smith Machine Bench Press|Smith Machine", "Plyometric Push-up|Bodyweight", "Wide Grip Push-up|Bodyweight", "Banded Push-up|Band",
    # Forearms
    "Wrist Curl|Barbell", "Reverse Wrist Curl|Barbell", "Farmers Walk|Dumbbell", "Dead Hang|Bodyweight", "Plate Pinch|Plates", "Reverse Curl (Forearm Focus)|EZ Bar", "Hammer Curl (Forearm Focus)|Dumbbell", "Behind the Back Wrist Curl|Barbell", "Towel Pull-up|Bodyweight", "Fat Gripz Curls|Dumbbell", "Wrist Roller|Equipment", "Dumbbell Wrist Curl|Dumbbell", "Dumbbell Reverse Wrist Curl|Dumbbell", "Zottman Curl (Forearm Focus)|Dumbbell", "Grip Crusher|Gripper", "Kettlebell Bottoms-up Press|Kettlebell", "Hex Dumbbell Hold|Dumbbell", "Rope Climb|Rope", "Cable Wrist Curl|Cable", "Cable Reverse Wrist Curl|Cable",
    # Full Body
    "Barbell Thruster|Barbell", "Dumbbell Thruster|Dumbbell", "Clean and Jerk|Barbell", "Power Clean|Barbell", "Snatch|Barbell", "Turkish Get-Up|Kettlebell", "Devil Press|Dumbbell", "Man Maker|Dumbbell", "Burpee Pull-up|Bodyweight", "Kettlebell Swing|Kettlebell", "Medicine Ball Slam|Medicine Ball", "Wall Ball|Medicine Ball", "Tire Flip|Tire", "Sled Push/Pull|Sled", "Bear Crawl|Full Body", "Rowing (Ergometer)|Machine", "Renegade Row|Dumbbell", "Dumbbell Snatch|Dumbbell", "Clean and Press|Barbell", "Kettlebell Clean and Press|Kettlebell",
    # Glutes
    "Barbell Hip Thrust|Barbell", "Glute Bridge|Bodyweight", "Bulgarian Split Squat|Dumbbell", "Romanian Deadlift|Barbell", "Cable Pull-through|Cable", "Kettlebell Swing (Glute Focus)|Kettlebell", "Step-up|Box", "Reverse Lunge|Dumbbell", "Walking Lunge|Dumbbell", "Curtsy Lunge (Glute Focus)|Dumbbell", "Good Morning|Barbell", "Donkey Kicks|Bodyweight", "Machine Glute Kickback|Machine", "Cable Glute Kickback|Cable", "Banded Glute Bridge|Band", "Frog Pumps|Bodyweight", "Hip Abduction Machine|Machine", "Banded Side Step|Band", "Single Leg Hip Thrust|Bodyweight", "Deficit Reverse Lunge|Dumbbell",
    # Hamstrings
    "Romanian Deadlift (Hamstring Focus)|Barbell", "Stiff-Legged Deadlift|Barbell", "Seated Leg Curl|Machine", "Lying Leg Curl|Machine", "Standing Leg Curl|Machine", "Nordic Hamstring Curl|Bodyweight", "Glute-Ham Raise|Machine", "Good Morning (Hamstring Focus)|Barbell", "Dumbbell RDL|Dumbbell", "Kettlebell Swing|Kettlebell", "Slider Leg Curl|Sliders", "Swiss Ball Leg Curl|Swiss Ball", "Cable Pull-through|Cable", "Banded Leg Curl|Band", "Single Leg RDL|Dumbbell", "Reverse Hyperextension|Machine", "Sumo Deadlift|Barbell", "Trap Bar Deadlift|Trap Bar", "Broad Jumps|Bodyweight", "Sprints (Hamstring Focus)|Bodyweight",
    # Lats
    "Pull-up|Bodyweight", "Lat Pulldown|Cable", "Barbell Row|Barbell", "Dumbbell Row|Dumbbell", "T-Bar Row|Barbell", "Seated Cable Row|Cable", "Straight Arm Pulldown|Cable", "Meadows Row|Barbell", "Pendlay Row|Barbell", "Chin-up|Bodyweight", "Neutral Grip Pull-up|Bodyweight", "Machine Row|Machine", "V-Bar Pulldown|Cable", "Reverse Grip Lat Pulldown|Cable", "Dumbbell Pullover|Dumbbell", "Cable Pullover|Cable", "Renegade Row (Lat Focus)|Dumbbell", "Yates Row|Barbell", "Kroc Row|Dumbbell", "Inverted Row|Bodyweight",
    # Lower Back
    "Deadlift|Barbell", "Hyperextension (Back Extension)|Machine", "Good Morning (Lower Back Focus)|Barbell", "Superman Hold|Bodyweight", "Reverse Hyperextension (Lower Back)|Machine", "Bird Dog|Bodyweight", "Romanian Deadlift (Lower Back)|Barbell", "Rack Pull|Barbell", "Jefferson Curl|Barbell", "Deficit Deadlift|Barbell", "Kettlebell Swing (Lower Back)|Kettlebell", "Seated Good Morning|Barbell", "Cable Pull-through (Lower Back)|Cable", "Banded Good Morning|Band", "Plank Hold|Bodyweight", "Swiss Ball Back Extension|Swiss Ball", "Barbell Row (Strict)|Barbell", "Aquaman|Bodyweight", "GHD Back Extension|GHD Machine", "Cat-Cow Stretch|Bodyweight",
    # Quadriceps
    "Barbell Squat|Barbell", "Front Squat|Barbell", "Leg Press|Machine", "Leg Extension|Machine", "Hack Squat|Machine", "Bulgarian Split Squat (Quad Focus)|Dumbbell", "Goblet Squat|Dumbbell", "Lunge|Bodyweight", "Walking Lunge (Quad Focus)|Dumbbell", "Pistol Squat|Bodyweight", "Sissy Squat|Bodyweight", "Zercher Squat|Barbell", "Smith Machine Squat|Smith Machine", "Cyclist Squat|Dumbbell", "Step-up (Quad Focus)|Box", "Box Squat|Barbell", "Wall Sit|Bodyweight", "Overhead Squat|Barbell", "Tire Flips (Quad Focus)|Tire", "Jump Squat|Bodyweight",
    # Shoulders
    "Overhead Press|Barbell", "Dumbbell Shoulder Press|Dumbbell", "Arnold Press|Dumbbell", "Lateral Raise|Dumbbell", "Front Raise|Dumbbell", "Reverse Pec Deck|Machine", "Face Pull|Cable", "Upright Row|Barbell", "Push Press|Barbell", "Cable Lateral Raise|Cable", "Machine Shoulder Press|Machine", "Bent-Over Lateral Raise|Dumbbell", "Military Press|Barbell", "Landmine Press|Barbell", "Pike Push-up|Bodyweight", "Handstand Push-up|Bodyweight", "Cuban Press|Dumbbell", "Seated Barbell Press|Barbell", "Kettlebell Press|Kettlebell", "Bradford Press|Barbell",
    # Traps
    "Barbell Shrug|Barbell", "Dumbbell Shrug|Dumbbell", "Trap Bar Shrug|Trap Bar", "Smith Machine Shrug|Smith Machine", "Cable Shrug|Cable", "Upright Row (Trap Focus)|Barbell", "Face Pull (Trap Focus)|Cable", "Farmer''s Walk (Trap Focus)|Dumbbell", "Power Clean (Trap Focus)|Barbell", "Snatch Pull|Barbell", "Clean Pull|Barbell", "Rack Pull (Trap Focus)|Barbell", "Overhead Shrug|Barbell", "Kelso Shrug|Dumbbell", "Y-Raise|Dumbbell", "Silverback Shrug|Dumbbell", "Seated Dumbbell Shrug|Dumbbell", "Behind the Back Barbell Shrug|Barbell", "Meadows Shrug|Dumbbell", "Incline Dumbbell Shrug|Dumbbell",
    # Triceps
    "Triceps Pushdown|Cable", "Overhead Triceps Extension|Dumbbell", "Skullcrusher|EZ Bar", "Close-Grip Bench Press|Barbell", "Triceps Dip|Bodyweight", "Machine Triceps Extension|Machine", "Dumbbell Kickback|Dumbbell", "Cable Kickback|Cable", "Rope Pushdown|Cable", "V-Bar Pushdown|Cable", "French Press|EZ Bar", "Diamond Push-up|Bodyweight", "JM Press|Barbell", "Tate Press|Dumbbell", "Single-arm Reverse Pulldown|Cable", "Band Triceps Extension|Band", "Rolling Dumbbell Extension|Dumbbell", "Bench Dip|Bodyweight", "Floor Press (Tricep Focus)|Barbell", "Board Press|Barbell",
    # Upper Back
    "Barbell Row (Upper Back Focus)|Barbell", "T-Bar Row (Wide Grip)|Barbell", "Dumbbell Row (Elbows Out)|Dumbbell", "Seated Cable Row (Wide Grip)|Cable", "Chest-Supported Row|Machine", "Seal Row|Barbell", "Face Pull (Upper Back Focus)|Cable", "Reverse Pec Deck (Upper Back Focus)|Machine", "Bent-Over Lateral Raise (Heavy)|Dumbbell", "Band Pull-Apart|Band", "Meadows Row (Upper Back Focus)|Barbell", "Pendlay Row (Upper Back Focus)|Barbell", "Inverted Row (Wide Grip)|Bodyweight", "Machine High Row|Machine", "Y-W-T Raises|Bodyweight", "Prone Cobra|Bodyweight", "Kroc Row (Upper Back Focus)|Dumbbell", "Scapular Pull-up|Bodyweight", "Renegade Row (Upper Back Focus)|Dumbbell", "Snatch Grip Deficit Deadlift|Barbell"
]

with open("/Users/prathamkitawat/formFit/backend/src/main/resources/seed_exercises.sql", "w") as f:
    f.write("TRUNCATE TABLE workout_sets, workout_exercises, workouts, template_exercises, templates, exercise_muscle_groups, exercises, muscle_groups CASCADE;\n\n")
    
    # Muscle Groups
    f.write("-- =====================\n-- MUSCLE GROUPS\n-- =====================\n")
    mg_values = []
    for i, mg in enumerate(muscle_groups, start=1):
        mg_values.append(f"({i}, '{mg}')")
    f.write("INSERT INTO muscle_groups (id, name) VALUES\n" + ",\n".join(mg_values) + ";\n")
    f.write(f"SELECT setval('muscle_group_id_seq', {len(muscle_groups) + 10});\n\n")

    # Exercises
    f.write("-- =====================\n-- EXERCISES\n-- =====================\n")
    ex_values = []
    em_values = []
    ex_id = 1
    mg_id = 1
    for data in exercises_data:
        name, equip = data.split('|')
        name = name.replace("'", "''")
        ex_values.append(f"({ex_id}, '{name}', '{equip}', null, null, null, false)")
        em_values.append(f"({ex_id}, {mg_id})")
        
        ex_id += 1
        if (ex_id - 1) % 20 == 0:
            mg_id += 1
            
    # Batch inserts
    chunk_size = 50
    for i in range(0, len(ex_values), chunk_size):
        chunk = ex_values[i:i+chunk_size]
        f.write("INSERT INTO exercises (id, name, equipment, image_url, video_url, user_id, archived) VALUES\n" + ",\n".join(chunk) + ";\n")
    f.write(f"SELECT setval('exercise_id_seq', {len(exercises_data) + 10});\n\n")
    
    # Exercise Muscle Groups
    f.write("-- =====================\n-- EXERCISE MUSCLE GROUPS\n-- =====================\n")
    for i in range(0, len(em_values), chunk_size):
        chunk = em_values[i:i+chunk_size]
        f.write("INSERT INTO exercise_muscle_groups (exercise_id, muscle_group_id) VALUES\n" + ",\n".join(chunk) + ";\n")
    f.write("\n")
    
    # ID Mappings for Workouts (0-indexed for simplicity in code)
    # Chest = 101+, Biceps = 41+, Back (Lats) = 201+, Quads = 241+, Hamstrings = 181+, Traps = 281+
    # 101 Barbell Bench Press (Chest)
    # 241 Barbell Squat (Quads)
    # 221 Deadlift (Lower Back)
    # 201 Pull-up (Lats)
    # 261 Overhead Press (Shoulders)
    # 203 Barbell Row (Lats)
    # 42 Dumbbell Curl (Biceps)
    # 301 Tricep Pushdown (Triceps)
    # 243 Leg Press (Quads)
    # 3 Plank (Abdominals)

    workouts = [
        # Push Day: Bench, OHP, Triceps (101, 261, 301)
        (1, 'Push Day', '2026-01-10 09:00:00', '2026-01-10 10:05:00', 65, [101, 261, 301]),
        # Pull Day: Pull-up, Barbell Row, Curl (201, 203, 42)
        (2, 'Pull Day', '2026-01-13 08:00:00', '2026-01-13 09:00:00', 60, [201, 203, 42]),
        # Leg Day: Squat, Leg Press (241, 243)
        (3, 'Leg Day', '2026-01-15 07:30:00', '2026-01-15 08:45:00', 75, [241, 243]),
        (4, 'Push Day', '2026-02-03 09:00:00', '2026-02-03 10:00:00', 60, [101, 261, 301]),
        (5, 'Pull Day', '2026-02-06 08:00:00', '2026-02-06 09:10:00', 70, [201, 203, 42]),
        (6, 'Leg Day', '2026-02-10 07:00:00', '2026-02-10 08:15:00', 75, [241, 243]),
        # Full Body: Bench, Squat, Pull Up, Plank (101, 241, 201, 3)
        (7, 'Full Body', '2026-02-14 10:00:00', '2026-02-14 11:20:00', 80, [101, 241, 201, 3]),
        (8, 'Push Day', '2026-03-01 09:00:00', '2026-03-01 10:00:00', 60, [101, 261, 301]),
        (9, 'Pull Day', '2026-03-05 08:00:00', '2026-03-05 09:00:00', 60, [201, 203, 42]),
        (10, 'Leg Day', '2026-03-10 07:30:00', '2026-03-10 08:45:00', 75, [241, 243]),
        (11, 'Push Day', '2026-03-15 09:00:00', '2026-03-15 10:10:00', 70, [101, 261, 301]),
        (12, 'Full Body', '2026-03-18 10:00:00', '2026-03-18 11:15:00', 75, [101, 241, 201, 3])
    ]

    f.write("-- =====================\n-- WORKOUTS\n-- =====================\n")
    w_values = []
    # Hardcode user_id = 1 for mock data (Assuming user 1 exists or they will run app with dummy login)
    # Wait, the frontend handles user authentication so the workouts might not match the current logged-in user!
    # They requested "user_id, name, workout_status" directly from their prompt.
    for w in workouts:
        wid, name, start, end, dur, _ = w
        w_values.append(f"({wid}, 1, '{name}', 'COMPLETED', '{start}', '{end}', {dur})")
    
    f.write("INSERT INTO workouts (id, user_id, name, workout_status, started_at, ended_at, duration) VALUES\n" + ",\n".join(w_values) + ";\n")
    f.write("SELECT setval('workout_id_seq', 100);\n\n")

    f.write("-- =====================\n-- WORKOUT EXERCISES & SETS\n-- =====================\n")
    we_values = []
    ws_values = []
    we_id = 1
    ws_id = 1
    
    for w in workouts:
        wid, _, _, _, _, ex_ids = w
        order = 1
        for ex_id in ex_ids:
            we_values.append(f"({we_id}, {wid}, {ex_id}, {order}, null)")
            
            # Simple 3 sets logic
            weight = 50.0 + (ex_id % 10) * 5
            reps = 8 + (ex_id % 4)
            for set_num in range(1, 4):
                ws_values.append(f"({ws_id}, {we_id}, {set_num}, {weight}, {reps}, false)")
                ws_id += 1
                
            we_id += 1
            order += 1

    for i in range(0, len(we_values), chunk_size):
        chunk = we_values[i:i+chunk_size]
        f.write("INSERT INTO workout_exercises (id, workout_id, exercise_id, order_index, notes) VALUES\n" + ",\n".join(chunk) + ";\n")
    f.write(f"SELECT setval('workout_exercise_id_seq', {we_id + 10});\n\n")
    
    for i in range(0, len(ws_values), chunk_size):
        chunk = ws_values[i:i+chunk_size]
        f.write("INSERT INTO workout_sets (id, workout_exercise_id, set_number, weight, reps, is_drop_set) VALUES\n" + ",\n".join(chunk) + ";\n")
    f.write(f"SELECT setval('workout_set_id_seq', {ws_id + 10});\n\n")

    f.write("-- Done.\n")
    print("SQL seed file generated.")
