import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ManageRobots manageRobots = new ManageRobots();
        while (true) {
            String input = scanner.nextLine().trim();
            String[] firstParameters = input.split(" ");
            if (input.equals("end"))
                break;
            if (firstParameters[0].equals("create") && firstParameters[1].equals("security") && firstParameters[2].equals("robot"))
                manageRobots.createSecurityRobot(firstParameters);
            else if (firstParameters[0].equals("create") && firstParameters[1].equals("delivery") && firstParameters[2].equals("robot"))
                manageRobots.createDeliveryRobot(firstParameters);
            else if (firstParameters[0].equals("create") && firstParameters[1].equals("cleaning") && firstParameters[2].equals("robot"))
                manageRobots.createCleaningRobot(firstParameters);
            else if (firstParameters[0].equals("clean") && firstParameters[1].equals("robot"))
                manageRobots.clean(Integer.parseInt(firstParameters[2]), Integer.parseInt(firstParameters[3]));
            else if (firstParameters[0].equals("perform") && firstParameters[1].equals("task") && firstParameters[2].equals("robot"))
                manageRobots.perform(Integer.parseInt(firstParameters[3]));
            else if (firstParameters[0].equals("attack") && firstParameters[1].equals("robot"))
                manageRobots.attack(Integer.parseInt(firstParameters[2]), Integer.parseInt(firstParameters[3]));
            else if (firstParameters[0].equals("deliver") && firstParameters[1].equals("robot"))
                manageRobots.deliver(Integer.parseInt(firstParameters[2]));
            else if (input.equals("get all robots"))
                manageRobots.getAllRobots();
            else System.out.println("unknown command");
        }
    }

}

abstract class Robot {
    private int id;

    public Robot(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public abstract String getType();
}

class CleaningRobot extends Robot {
    private String type;
    private int id;
    private int numTask;
    private ArrayList<Integer> areas = new ArrayList<>();

    public CleaningRobot(int id, int numTask, ArrayList<Integer> areas) {
        super(id);
        this.type = "cleaning";
        this.numTask = numTask;
        this.areas = areas;

    }

    public int getNumTask() {
        return this.numTask;
    }

    public void reduceNumTask() {
        this.numTask = this.numTask - 1;
    }

    public ArrayList<Integer> getAreas() {
        return this.areas;
    }

    @Override
    public String getType() {
        return this.type;
    }
}

class SecurityRobot extends Robot {
    private String type;
    private int power;
    private int live;

    public SecurityRobot(int id, int power, int live) {
        super(id);
        this.type = "security";
        this.power = power;
        this.live = live;
    }

    public int getPower() {
        return this.power;
    }

    public int getLive() {
        return this.live;
    }

    public void reduceLive() {
        this.live = this.live - 1;
    }

    @Override
    public String getType() {
        return this.type;
    }
}

class DeliveryRobot extends Robot {
    private String type;
    private String vehicle;

    public DeliveryRobot(int id, String vehicle) {
        super(id);
        this.type = "delivery";
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return this.vehicle;
    }

    @Override
    public String getType() {
        return this.type;
    }
}

abstract class RobotFactory {
    abstract public Robot createRobot(String[] parametersForMakingRobot);
}

class SecurityRobotFactory extends RobotFactory {
    @Override
    public Robot createRobot(String[] parametersForMakingRobot) {
        int id = Integer.parseInt(parametersForMakingRobot[0]);
        int power = Integer.parseInt(parametersForMakingRobot[1]);
        int live = Integer.parseInt(parametersForMakingRobot[2]);
        Robot robot = new SecurityRobot(id, power, live);
        return robot;

    }
}

class CleaningRobotFactory extends RobotFactory {
    @Override
    public Robot createRobot(String[] parametersForMakingRobot) {
        ArrayList<Integer> areas = new ArrayList<>();
        int id = Integer.parseInt(parametersForMakingRobot[0]);
        int numTask = Integer.parseInt(parametersForMakingRobot[1]);
        for (int i = 2; i < parametersForMakingRobot.length; i++) {
            areas.add(Integer.parseInt(parametersForMakingRobot[i]));
        }
        Robot robot = new CleaningRobot(id, numTask, areas);
        return robot;
    }
}

class DeliveryRobotFactory extends RobotFactory {

    @Override
    public Robot createRobot(String[] parametersForMakingRobot) {
        int id = Integer.parseInt(parametersForMakingRobot[0]);
        String vehicle = parametersForMakingRobot[1];
        Robot robot = new DeliveryRobot(id, vehicle);
        return robot;
    }
}

class ManageRobots {
    private HashMap<Integer, Robot> allRobots = new HashMap<>();

    public void createSecurityRobot(String[] firstParams) {
        String[] secondParams = new String[firstParams.length - 3];
        System.arraycopy(firstParams, 3, secondParams, 0, firstParams.length - 3);
        RobotFactory robotFactory = new SecurityRobotFactory();
        Robot robot = robotFactory.createRobot(secondParams);
        allRobots.put(robot.getId(), robot);
    }

    public void createCleaningRobot(String[] firstParams) {
        String[] secondParams = new String[firstParams.length - 3];
        System.arraycopy(firstParams, 3, secondParams, 0, firstParams.length - 3);
        RobotFactory robotFactory = new CleaningRobotFactory();
        Robot robot = robotFactory.createRobot(secondParams);
        allRobots.put(robot.getId(), robot);
    }

    public void createDeliveryRobot(String[] firstParams) {
        String[] secondParams = new String[firstParams.length - 3];
        System.arraycopy(firstParams, 3, secondParams, 0, firstParams.length - 3);
        RobotFactory robotFactory = new DeliveryRobotFactory();
        Robot robot = robotFactory.createRobot(secondParams);
        allRobots.put(robot.getId(), robot);
    }

    public void clean(int id, int area) {
        if (!allRobots.containsKey(id) ||
                !(allRobots.containsKey(id) && allRobots.get(id) instanceof CleaningRobot)) {
            System.out.println("invalid robot id");
            return;
        }
        if (allRobots.containsKey(id) && allRobots.get(id) instanceof CleaningRobot) {
            CleaningRobot cleaningRobot = (CleaningRobot) allRobots.get(id);
            if (cleaningRobot.getNumTask() == 0) {
                System.out.println("this robot is retired");
                return;
            }
            if (!cleaningRobot.getAreas().contains(area)) {
                System.out.println("invalid area");
                return;
            }
            cleaningRobot.reduceNumTask();
            System.out.println("cleaning robot " + id + " cleaned the area");
        }
    }

    public void perform(int id) {
        if (!allRobots.containsKey(id) ||
                (allRobots.containsKey(id) && !(allRobots.get(id) instanceof SecurityRobot))) {
            System.out.println("invalid robot id");
            return;
        }
        System.out.println("security robot is monitoring");
    }

    public void attack(int id, int enemyPower) {
        if (!allRobots.containsKey(id) ||
                (allRobots.containsKey(id) && !(allRobots.get(id) instanceof SecurityRobot))) {
            System.out.println("invalid robot id");
            return;
        }
        if (allRobots.containsKey(id) && allRobots.get(id) instanceof SecurityRobot) {
            SecurityRobot securityRobot = (SecurityRobot) allRobots.get(id);
            if (securityRobot.getPower() >= enemyPower) {
                System.out.println("attack was successful");
                return;
            }
            if (securityRobot.getPower() < enemyPower) {
                securityRobot.reduceLive();
                if (securityRobot.getLive() == 0) {
                    allRobots.remove(id);
                }
                System.out.println("attack was unsuccessful");
                return;
            }
        }
    }

    public void deliver(int id) {
        if (!allRobots.containsKey(id) ||
                (allRobots.containsKey(id) && !(allRobots.get(id) instanceof DeliveryRobot))) {
            System.out.println("invalid robot id");
            return;
        }
        if (allRobots.containsKey(id) && allRobots.get(id) instanceof DeliveryRobot) {
            DeliveryRobot deliveryRobot = (DeliveryRobot) allRobots.get(id);
            System.out.println("delivery robot " + id + " sent the pocket by " + deliveryRobot.getVehicle());
        }
    }

    public void getAllRobots() {
        for (HashMap.Entry<Integer, Robot> entry : allRobots.entrySet()) {
            System.out.println("robot " + entry.getKey() + " " + entry.getValue().getType());
        }
    }
}
