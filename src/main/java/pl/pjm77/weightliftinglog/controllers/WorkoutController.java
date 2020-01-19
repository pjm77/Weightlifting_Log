package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.models.File;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.services.FileService;
import pl.pjm77.weightliftinglog.services.UserService;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import java.util.ArrayList;
import java.util.LinkedList;

import static pl.pjm77.weightliftinglog.services.UserService.checkLoggedInUserForAdminRights;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService, FileService fileService) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String addWorkoutGet(Model model) {
        User user = userService.findUserByEmail(UserService.getLoggedInUsersEmail());
        model.addAttribute("user", user.getEmail());
        model.addAttribute("userName", user.getName());
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-workout-details");
        model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
        return "home";
    }

    @ResponseBody
    @GetMapping("/{workoutId}")
    public WorkoutDeserialized getWorkoutById(@PathVariable long workoutId) {
        return workoutService.findWorkoutById(workoutId);
    }

//    @GetMapping(value = "/files/{workoutId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public @ResponseBody ArrayList<byte[]> getFilesByWorkoutId(@PathVariable long workoutId) {
//        ArrayList<File> filesFromDatabase = fileService.getWorkoutFiles(workoutId);
//        ArrayList<byte[]> filesToSend = new ArrayList<>();
//        filesFromDatabase.forEach((file) -> filesToSend.add(file.getContent()));
//        return filesToSend;
//    }

    @ResponseBody
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addWorkoutPost(@RequestPart("workout") WorkoutDeserialized workoutDeserialized,
                               @RequestPart("filesToRemove") ArrayList<String> filesToRemove,
                               @RequestPart("filesToUpload") LinkedList<MultipartFile> filesToUpload) {
        workoutDeserialized.setUser
                (userService.findUserByEmail(UserService.getLoggedInUsersEmail()));
        filesToRemove.forEach(System.out::println);
        fileService.storeAllFiles(workoutService.saveWorkout(workoutDeserialized), filesToUpload);
    }

    @ResponseBody
    @DeleteMapping("/{workoutId}")
    public void deleteWorkout(@PathVariable long workoutId) {
        workoutService.deleteWorkout(workoutId);
    }
}