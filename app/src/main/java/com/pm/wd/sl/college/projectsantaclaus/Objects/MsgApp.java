package com.pm.wd.sl.college.projectsantaclaus.Objects;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MsgApp extends Application {
    private static MsgApp instance;
    public final List<Message> messages = new ArrayList<>();
    //    public MessageAdapter msgAdapter;
    public User user;
    public List<MessageRefresh> msgRefresh = new ArrayList<>();

    public static MsgApp instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public interface MessageRefresh {
        void onRefresh();

        boolean once();
    }

    /*
    public void refreshVisits() {
        String url;
        if (employee != null) {
            if (employee.getDesignation().equalsIgnoreCase(Constants.SECURITY_DESIGNATION)) {
                url = Constants.URL + "visit-log?key=" + Constants.API_TOKEN + "&id=0";
            } else {
                url = Constants.URL + "visit-log?key=" +
                        Constants.API_TOKEN + "&id=" + employee.getId();
            }
            ServerConnector connector = new ServerConnector(instance(), url, this);
            connector.makeQuery();
        }
    }

    public void refreshVisits(Employee emp) {
        if (emp == null) {
            refreshVisits();
            return;
        }

        employee = emp;

        String url;

        if (emp.getDesignation().equalsIgnoreCase(Constants.SECURITY_DESIGNATION)) {
            url = Constants.URL + "visit-log?key=" + Constants.API_TOKEN + "&id=0";
        } else {
            url = Constants.URL + "visit-log?key=" +
                    Constants.API_TOKEN + "&id=" + emp.getId();
        }
        ServerConnector connector = new ServerConnector(instance(), url, this);
        connector.makeQuery();
    }

    @Override
    public void onResponse(JSONObject object) {
        visits.clear();
        try {
            JSONArray jsonArray = object.getJSONArray(Constants.JSON_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject oneObject = jsonArray.getJSONObject(i);
                Visitor visitor = new Visitor(
                        oneObject.getString("v_fName"),
                        oneObject.getString("v_lName"),
                        oneObject.getString("v_mobile_number"),
                        "",
                        oneObject.getInt(Constants.JSON_PARKING) == 0);
                String timeDate = oneObject.getString(Constants.JSON_TIME_STAMP);
                String date = timeDate.split(",")[0];
                String time = timeDate.split(",")[1];
                Visit visit;
                if (employee.getDesignation().equalsIgnoreCase(Constants.SECURITY_DESIGNATION)) {
                    Employee employee = new Employee(oneObject.getInt(Constants.JSON_EMPLOYEE_ID),
                            oneObject.getString(Constants.JSON_FIRST_NAME),
                            oneObject.getString(Constants.JSON_LAST_NAME),
                            oneObject.getString(Constants.JSON_EMPLOYEE_COMPANY),
                            oneObject.getString(Constants.JSON_EMPLOYEEE_DESIGNATION),
                            oneObject.getString(Constants.JSON_EMPLOYEE_LOCATION),
                            oneObject.getString(Constants.JSON_MOBILE_NUMBER_COLUMN),
                            oneObject.getString(Constants.JSON_EMPLOYEE_CURRENT_STATUS));
                    visit = new Visit(visitor, date, oneObject.getInt(Constants.JSON_STATUS),
                            time, oneObject.getString(Constants.JSON_VISIT_PURPOSE),
                            employee);
                } else {
                    visit = new Visit(visitor, date, oneObject.getInt(Constants.JSON_STATUS), time,
                            oneObject.getString(Constants.JSON_VISIT_PURPOSE),
                            employee);
                }
                visits.add(visit);
            }
            if (visits.isEmpty()) {

            }
            visitAdapter = new VisitAdapter(this, visits);
            visitAdapter.notifyDataSetChanged();

            ListIterator i = visitLogRefresh.listIterator();
            while (i.hasNext()) {
                VisitLogRefresh vlogref = (VisitLogRefresh) i.next();

                vlogref.onRefresh();

                if (vlogref.once())
                    i.remove();
            }
        } catch (JSONException e) {
            Messages.logMessage(HXApplication.class.getSimpleName(), e.toString());
            Messages.toastMessage(instance(), "Something went wrong.");
        }
    }

    @Override
    public void onErrorResponse(VolleyError e) {
        Messages.toastMessage(instance(), "Something went wrong.");
        Messages.logMessage(HXApplication.class.getSimpleName(), Arrays.toString(e.getStackTrace()));
    }
    */
}
