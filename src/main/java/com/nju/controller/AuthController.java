package com.nju.controller;

import com.nju.data.DepartBRiskImpl;
import com.nju.model.Course;
import com.nju.model.Risk;
import com.nju.model.RiskPlan;
import com.nju.model.Student;
import com.nju.service.CourseService;
import com.nju.service.StudentService;
import com.nju.service.RiskService;
import com.nju.service.impl.RiskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by 传旺 on 2016/5/23.
 */

@Controller
public class AuthController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;


   // private RiskService riskService;

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public String login(){
        courseService.getCourses(1);
        return "/login";
    }

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public String postlogin(@RequestParam  String username, @RequestParam String password, HttpSession session){
        System.out.println(username);
        System.out.println(password);
        if(password.equals(studentService.getStudentPass(username))){
            session.setAttribute("id", username);
            return "redirect:/index";
        }
        return "/login";
    }

    @RequestMapping(value = "/logout" , method = RequestMethod.GET)
    public String logout(HttpSession session){
        Enumeration<String> em = session.getAttributeNames();
        while (em.hasMoreElements()) {
            session.removeAttribute(em.nextElement().toString());
        }
        return "/login";
    }

    @RequestMapping(value = "/getCourses", method = RequestMethod.POST)
    @ResponseBody
    public List<Course> getCourses(HttpSession session){
        int id = Integer.parseInt(session.getAttribute("id").toString());
        return courseService.getCourses(id);
    }

    @RequestMapping(value = "/getOtherCourses", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<Course>> getOtherCourse(HttpSession session){
        int id = Integer.parseInt(session.getAttribute("id").toString());
        return courseService.getOtherCourses(id);
    }

    @RequestMapping(value = "/getMyCourses", method = RequestMethod.POST)
    @ResponseBody
    public List<Course> getMyCourses(HttpSession session){
        int id = Integer.parseInt(session.getAttribute("id").toString());
        return courseService.getMyCourses(id);
    }

    @RequestMapping(value = "/getAllStudents", method = RequestMethod.POST)
    @ResponseBody
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @RequestMapping(value = "/chooseCourse", method = RequestMethod.POST)
    @ResponseBody
    public boolean chooseCourse(@RequestParam int courseId, @RequestParam String department, HttpSession session){
        int studentId = Integer.parseInt(session.getAttribute("id").toString());
        return courseService.chooseCourse(studentId, courseId, department);
    }

    @RequestMapping(value = "/dropCourse", method = RequestMethod.POST)
    @ResponseBody
    public boolean dropCourse(@RequestParam int courseId, @RequestParam String department, HttpSession session){
        int studentId = Integer.parseInt(session.getAttribute("id").toString());
        return courseService.dropCourse(studentId, courseId, department);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpSession session){
        if(session.getAttribute("id") == null) {
            return "/login";
        }
        return "/index";
    }

    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public String course(){
        return "/course";
    }

    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public String plan(){
        return "/plan";
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String students(){
        return "/students";
    }

    @RequestMapping(value = "/getAllRiskPlan", method = RequestMethod.POST)
    @ResponseBody
    public List<RiskPlan> getAllRiskPlan(){
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        System.out.println("  planget");
        return riskService.getAllRiskPlan();
    }

    @RequestMapping(value = "/getAllRisks", method = RequestMethod.POST)
    @ResponseBody
    public List<Risk> getAllRisks(){
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        return riskService.getAllRisks();
    }



    @RequestMapping(value = "/getRecByTime", method = RequestMethod.POST)
    @ResponseBody
    public List<Risk> getRecByTime(String startTime,String endTime){
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        return riskService.getRecByTime(startTime,endTime);
    }

    @RequestMapping(value = "/getChangeByTime", method = RequestMethod.POST)
    @ResponseBody
    public List<Risk> getChangeByTime(String startTime,String endTime){
        DepartBRiskImpl riskService=new DepartBRiskImpl();

        return riskService.getChangeByTime(startTime,endTime);
    }


    @RequestMapping(value = "/addRisk", method = RequestMethod.POST)
    @ResponseBody
    public boolean addRisk(@RequestParam String riskId,@RequestParam String riskName,@RequestParam String riskContent,@RequestParam String riskPossibility,@RequestParam String riskLevel,@RequestParam String riskGate, HttpSession session){

        Risk risk = new Risk(0,riskName,riskContent,riskLevel, riskPossibility, riskGate, session.getAttribute("id").toString(), "",  getTime(),0,0) ;
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        riskService.addRisk(risk);
        return true;
    }



    @RequestMapping(value = "/addRiskToPlan", method = RequestMethod.POST)
    @ResponseBody
    public boolean addRiskToPlan(@RequestParam String risk_idlist,@RequestParam String plan_idlist){
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        riskService.addRiskToPlan(risk_idlist,plan_idlist);
        return true;
    }


    @RequestMapping(value = "/addRiskPlan", method = RequestMethod.POST)
    @ResponseBody
    public boolean addRiskPlan(@RequestParam String riskPlanId,@RequestParam String riskPlanName,@RequestParam String riskPlanContent, HttpSession session){

        RiskPlan riskplan = new RiskPlan(0,riskPlanName, session.getAttribute("id").toString(), riskPlanContent,   getTime()) ;
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        riskService.addRiskPlan(riskplan);
        return true;
    }



    @RequestMapping(value = "/followRisk", method = RequestMethod.POST)
    @ResponseBody
    public boolean followRisk(@RequestParam int risk_id, HttpSession session){

        DepartBRiskImpl riskService=new DepartBRiskImpl();
        System.out.print(risk_id+session.getAttribute("id").toString());
        riskService.followRisk(risk_id,session.getAttribute("id").toString());
        return true;
    }


    @RequestMapping(value = "/recRisk", method = RequestMethod.POST)
    @ResponseBody
    public boolean recRisk(@RequestParam int risk_id){

        DepartBRiskImpl riskService=new DepartBRiskImpl();

        riskService.RiskRec(risk_id);
        riskService.addRiskRec(risk_id,getTime());
        return true;
    }

    @RequestMapping(value = "/changeRisk", method = RequestMethod.POST)
    @ResponseBody
    public boolean changeRisk(@RequestParam int risk_id){

        DepartBRiskImpl riskService=new DepartBRiskImpl();

        riskService.RiskChange(risk_id);
        riskService.addRiskChange(risk_id,getTime());

        return true;
    }


    @RequestMapping(value = "/updateRisk", method = RequestMethod.POST)
    @ResponseBody
    public boolean updateRisk(@RequestParam int risk_id,@RequestParam String risk_name,@RequestParam String risk_content,@RequestParam String risk_gate,@RequestParam String risk_level,@RequestParam String risk_possibility){

        DepartBRiskImpl riskService=new DepartBRiskImpl();

        riskService.updateRisk(risk_id,risk_name,risk_content,risk_gate,risk_level,risk_possibility);
        return true;
    }




    private String getTime(){
        Date date = new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        String time=format.format(date);

        return time;
    }

    @RequestMapping(value = "/getRisk", method = RequestMethod.POST)
    @ResponseBody
    public Risk getRisk(int risk_id){
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        return riskService.getRisk(risk_id);
    }


    @RequestMapping(value = "/getRisksForPlan", method = RequestMethod.POST)
    @ResponseBody
    public List<Risk> getRisksForPlan(int PlanId){
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        return riskService.getRiskForPlan(PlanId);
    }

    @RequestMapping(value = "/deleteRisk", method = RequestMethod.POST)
    @ResponseBody
    public void deleteRisk(String risk_id){
        System.out.println(risk_id);
        int riskid=Integer.valueOf(risk_id);
        DepartBRiskImpl riskService=new DepartBRiskImpl();
        riskService.deleteRisk(riskid);
    }

}
