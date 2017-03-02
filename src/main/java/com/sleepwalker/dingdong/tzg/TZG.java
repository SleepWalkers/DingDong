package com.sleepwalker.dingdong.tzg;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepwalker.utils.HttpClientUtil;

public class TZG extends Thread {

    private static final String URL = "https://ios.tzg.cn/subject/list";

    @Override
    public void run() {
        int i = 1;

        while (true) {
            try {
                System.out.println("--------------------------------------------------------");
                System.out.println("开始第" + i + "次执行");
                String str = HttpClientUtil.post(URL, null);
                JSONObject json = JSON.parseObject(str);
                HomePage homePage = JSON.parseObject(json.getString("data"), HomePage.class);

                if (Double.parseDouble(homePage.getCurrentbaoAvailable()) > 0) {
                    System.out.println("铜钱宝可投资，可投金额为: " + homePage.getCurrentbaoAvailable() + "元");
                }
                List<SalaryList> salaryLists = homePage.getSalaryList();
                if (salaryLists != null) {
                    for (SalaryList salaryList : salaryLists) {
                        if (salaryList.isAvailable()) {
                            System.out.println(salaryList.getName() + "可投资，可投金额为: "
                                               + salaryList.getAvailableAmt() + "元");
                        }
                    }
                }

                List<Salary> salaries = homePage.getSalary();
                if (salaries != null) {
                    for (Salary salary : salaries) {
                        if (salary.isAvailable()) {
                            System.out.println(salary.getName() + "可投资，可投金额为: "
                                               + salary.getAvailableAmt() + "元");
                        }
                    }
                }

                List<ConductList> conductLists = homePage.getConductList();
                if (conductLists != null) {
                    for (ConductList conductList : conductLists) {
                        if (conductList.isAvailable()) {
                            System.out.println(conductList.getInvestDesc());
                        }
                    }
                }

                System.out.println("结束第" + i + "次执行");
                System.out.println("--------------------------------------------------------");
                System.out.println();
                System.out.println();

                i++;
                sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
