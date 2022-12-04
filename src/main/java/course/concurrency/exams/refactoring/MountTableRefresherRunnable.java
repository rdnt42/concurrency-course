package course.concurrency.exams.refactoring;

import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 04.12.2022
 * Time: 14:17
 */
public class MountTableRefresherRunnable implements Callable<Boolean> {
    private boolean success;

    private final String adminAddress;
    private final String managerAddress;
    private final String name;
    /** Admin server on which refreshed to be invoked. */
    private Others.MountTableManager manager;

    public MountTableRefresherRunnable(String adminAddress, String managerAddress, Others.MountTableManager manager) {
        this.adminAddress = adminAddress;
        this.manager = manager;
        this.name = "MountTableRefresh_" + adminAddress;
        this.managerAddress = managerAddress;
    }

    @Override
    public Boolean call() {
        success = manager.refresh(managerAddress);

        return success;
    }

    @Override
    public String toString() {
        return "MountTableRefreshThread [success=" + success + ", adminAddress="
                + adminAddress + "]";
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public boolean isSuccess() {
        return success;
    }
}
