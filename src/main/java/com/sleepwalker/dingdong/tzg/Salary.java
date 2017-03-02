package com.sleepwalker.dingdong.tzg;

public class Salary {
    private String modifiedTime;
    private String nextRewardRate;
    private String repayMode;
    private String description;
    private String availableAmt;
    private String isEnableInterestticket;
    private String type;
    private String percent;
    private String nextPerPeriodLockDays;
    private String investedAmt;
    private String totalPeriods;
    private String maxInvestAmt;
    private String nextMinInvestAmt;
    private String nextInterestRate;
    private String exitMode;
    private String maxInterestRate;
    private String id;
    private String state;
    private String interestRate;
    private String stepIncreaseAmt;
    private String rewardRate;
    private String minInvestAmt;
    private String profitMode;
    private String nextStepIncreaseAmt;
    private String trendImgUrl;
    private String isEnableCoupon;
    private String perPeriodLockDays;
    private String totalAmt;
    private String createTime;
    private String isEnableCashticket;
    private String nextMaxInvestDays;
    private String maxInvestDays;
    private String productIntroduction;
    private String name;
    private String contractId;
    private String nextMaxInterestRate;
    private String nextMaxInvestAmt;
    private String commonProblem;

    public boolean isAvailable() {
        if (Double.parseDouble(getAvailableAmt()) > 0) {
            return true;
        }
        return false;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getNextRewardRate() {
        return nextRewardRate;
    }

    public void setNextRewardRate(String nextRewardRate) {
        this.nextRewardRate = nextRewardRate;
    }

    public String getRepayMode() {
        return repayMode;
    }

    public void setRepayMode(String repayMode) {
        this.repayMode = repayMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(String availableAmt) {
        this.availableAmt = availableAmt;
    }

    public String getIsEnableInterestticket() {
        return isEnableInterestticket;
    }

    public void setIsEnableInterestticket(String isEnableInterestticket) {
        this.isEnableInterestticket = isEnableInterestticket;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getNextPerPeriodLockDays() {
        return nextPerPeriodLockDays;
    }

    public void setNextPerPeriodLockDays(String nextPerPeriodLockDays) {
        this.nextPerPeriodLockDays = nextPerPeriodLockDays;
    }

    public String getInvestedAmt() {
        return investedAmt;
    }

    public void setInvestedAmt(String investedAmt) {
        this.investedAmt = investedAmt;
    }

    public String getTotalPeriods() {
        return totalPeriods;
    }

    public void setTotalPeriods(String totalPeriods) {
        this.totalPeriods = totalPeriods;
    }

    public String getMaxInvestAmt() {
        return maxInvestAmt;
    }

    public void setMaxInvestAmt(String maxInvestAmt) {
        this.maxInvestAmt = maxInvestAmt;
    }

    public String getNextMinInvestAmt() {
        return nextMinInvestAmt;
    }

    public void setNextMinInvestAmt(String nextMinInvestAmt) {
        this.nextMinInvestAmt = nextMinInvestAmt;
    }

    public String getNextInterestRate() {
        return nextInterestRate;
    }

    public void setNextInterestRate(String nextInterestRate) {
        this.nextInterestRate = nextInterestRate;
    }

    public String getExitMode() {
        return exitMode;
    }

    public void setExitMode(String exitMode) {
        this.exitMode = exitMode;
    }

    public String getMaxInterestRate() {
        return maxInterestRate;
    }

    public void setMaxInterestRate(String maxInterestRate) {
        this.maxInterestRate = maxInterestRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getStepIncreaseAmt() {
        return stepIncreaseAmt;
    }

    public void setStepIncreaseAmt(String stepIncreaseAmt) {
        this.stepIncreaseAmt = stepIncreaseAmt;
    }

    public String getRewardRate() {
        return rewardRate;
    }

    public void setRewardRate(String rewardRate) {
        this.rewardRate = rewardRate;
    }

    public String getMinInvestAmt() {
        return minInvestAmt;
    }

    public void setMinInvestAmt(String minInvestAmt) {
        this.minInvestAmt = minInvestAmt;
    }

    public String getProfitMode() {
        return profitMode;
    }

    public void setProfitMode(String profitMode) {
        this.profitMode = profitMode;
    }

    public String getNextStepIncreaseAmt() {
        return nextStepIncreaseAmt;
    }

    public void setNextStepIncreaseAmt(String nextStepIncreaseAmt) {
        this.nextStepIncreaseAmt = nextStepIncreaseAmt;
    }

    public String getTrendImgUrl() {
        return trendImgUrl;
    }

    public void setTrendImgUrl(String trendImgUrl) {
        this.trendImgUrl = trendImgUrl;
    }

    public String getIsEnableCoupon() {
        return isEnableCoupon;
    }

    public void setIsEnableCoupon(String isEnableCoupon) {
        this.isEnableCoupon = isEnableCoupon;
    }

    public String getPerPeriodLockDays() {
        return perPeriodLockDays;
    }

    public void setPerPeriodLockDays(String perPeriodLockDays) {
        this.perPeriodLockDays = perPeriodLockDays;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsEnableCashticket() {
        return isEnableCashticket;
    }

    public void setIsEnableCashticket(String isEnableCashticket) {
        this.isEnableCashticket = isEnableCashticket;
    }

    public String getNextMaxInvestDays() {
        return nextMaxInvestDays;
    }

    public void setNextMaxInvestDays(String nextMaxInvestDays) {
        this.nextMaxInvestDays = nextMaxInvestDays;
    }

    public String getMaxInvestDays() {
        return maxInvestDays;
    }

    public void setMaxInvestDays(String maxInvestDays) {
        this.maxInvestDays = maxInvestDays;
    }

    public String getProductIntroduction() {
        return productIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        this.productIntroduction = productIntroduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getNextMaxInterestRate() {
        return nextMaxInterestRate;
    }

    public void setNextMaxInterestRate(String nextMaxInterestRate) {
        this.nextMaxInterestRate = nextMaxInterestRate;
    }

    public String getNextMaxInvestAmt() {
        return nextMaxInvestAmt;
    }

    public void setNextMaxInvestAmt(String nextMaxInvestAmt) {
        this.nextMaxInvestAmt = nextMaxInvestAmt;
    }

    public String getCommonProblem() {
        return commonProblem;
    }

    public void setCommonProblem(String commonProblem) {
        this.commonProblem = commonProblem;
    }
}
