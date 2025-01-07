package appsys.free.constru_app.remodel_repairs.responses;

public class PayrollValidationResponse {
    private boolean paymentStatus;
    private int typePayrollId;

    public PayrollValidationResponse(boolean paymentStatus, int typePayrollId) {
        this.paymentStatus = paymentStatus;
        this.typePayrollId = typePayrollId;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getTypePayrollId() {
        return typePayrollId;
    }

    public void setTypePayrollId(int typePayrollId) {
        this.typePayrollId = typePayrollId;
    }

}
