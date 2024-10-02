import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String first = scanner.nextLine();
        String[] firstLine = first.split(" ");
        int howManyOrders = Integer.parseInt(firstLine[0]);
        ArrayList<String> allIngredients = new ArrayList<>();
        makingAllIngredients(allIngredients);
        HashMap<String, Integer> mojoodi = new HashMap<>();
        int y = 1;
        double proft = 0;
        for (String ingredient : allIngredients) {
            mojoodi.put(ingredient, mojoodi.getOrDefault(ingredient, 0) + Integer.parseInt(firstLine[y]));
            y++;
        }
        Restaurant restaurant = Restaurant.getInstance(mojoodi);
        for (int j = 0; j < howManyOrders; j++) {
            String order = scanner.nextLine().trim();
            String[] eachOrder = order.split("and |also |with |extra | ");
            Order order1 = new Order(eachOrder, restaurant);
            if (!order1.canBeRecieved())
                System.out.println("Order Dismissed!");
            else {
                System.out.println("Order Completed!");
                proft += order1.getProfit();
            }
        }
        System.out.printf("The final profit is: %.1f", proft);
    }

    private static void makingAllIngredients(ArrayList<String> allIngredients) {
        allIngredients.add("Bacon");
        allIngredients.add("Basil");
        allIngredients.add("Bread");
        allIngredients.add("Cheese");
        allIngredients.add("Chicken");
        allIngredients.add("Corn");
        allIngredients.add("Dough");
        allIngredients.add("Egg");
        allIngredients.add("Fries");
        allIngredients.add("Garlic");
        allIngredients.add("Hamburger");
        allIngredients.add("Jalapeno");
        allIngredients.add("Lettuce");
        allIngredients.add("Mushroom");
        allIngredients.add("Olive");
        allIngredients.add("Onion");
        allIngredients.add("Pepper");
        allIngredients.add("Pepperoni");
        allIngredients.add("Pickles");
        allIngredients.add("Salami");
        allIngredients.add("Sauce");
        allIngredients.add("Tomato");
        allIngredients.add("Tuna");
    }
}

class Order {
    private String[] order;
    private Restaurant restaurant;
    private String size = new String();
    private double profit = 0;
    private HashMap<String, Integer> mojoodi = new HashMap<>();
    private HashMap<String, Integer> requiredIngredients = new HashMap<>();
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public Order(String[] order, Restaurant restaurant) {
        this.order = order;
        this.restaurant = restaurant;
        mojoodi = restaurant.getMojoodi();
        if (order[0].contains("Small") || order[0].contains("Medium") || order[0].contains("Large"))
            this.size = order[0];
    }

    public boolean canBeRecieved() {
        for (String ingredient : order) {
            if (ingredient.trim() != "") {
                if (ingredient.contains("Sandwich")) {
                    requiredIngredients.put("Bread", requiredIngredients.getOrDefault(ingredient, 0) + 2);
                    requiredIngredients.put("Sandwich", requiredIngredients.getOrDefault(ingredient, 0) + 1);
                } else if (ingredient.trim().contains("Pizza")) {
                    requiredIngredients.put("Dough", requiredIngredients.getOrDefault(ingredient, 0) + 1);
                    requiredIngredients.put("Pizza", requiredIngredients.getOrDefault(ingredient, 0) + 1);
                } else if (!ingredient.trim().contains("Sandwich") && !ingredient.contains("Pizza") && !ingredient.contains("Large")
                        && !ingredient.contains("Small") && !ingredient.contains("Medium")) {
                    requiredIngredients.put(ingredient, requiredIngredients.getOrDefault(ingredient, 0) + 1);
                }
            }
        }
        for (String ingredientName : requiredIngredients.keySet()) {
            if (ingredientName != "" && !ingredientName.contains("Sandwich") && !ingredientName.contains("Pizza") && !ingredientName.contains("Large")
                    && !ingredientName.contains("Small") && !ingredientName.contains("Medium")) {
                if (mojoodi.getOrDefault(ingredientName, 0) < requiredIngredients.get(ingredientName)) {
                    return false;
                }
            }
        }
        makeIngrediaentsList(requiredIngredients);
        reduceFromStock(requiredIngredients);
        return true;
    }

    private void reduceFromStock(HashMap<String, Integer> requiredIngredients) {
        HashMap<String, Integer> stock = restaurant.getMojoodi();
        for (String required : requiredIngredients.keySet()) {
            if (stock.containsKey(required.trim()) && !required.contains("Pizza") && !required.contains("Sandwich")) {
                int newQuantity = stock.get(required) - requiredIngredients.get(required);
                stock.put(required, newQuantity);
            }
        }
    }


    public void makeIngrediaentsList(HashMap<String, Integer> requiredIngredients) {

        for (String ingredient : requiredIngredients.keySet()) {
            if (ingredient.contains("Bacon")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Bacon", 2.2, this.order, this.restaurant));
            }
            if (ingredient.contains("Pizza")) {

                if (this.size.contains("Small"))
                    ingredients.add(new Ingredient("Pizza", 2, this.order, this.restaurant));
                if (this.size.contains("Medium"))
                    ingredients.add(new Ingredient("Pizza", 4, this.order, this.restaurant));
                if (this.size.contains("Large"))
                    ingredients.add(new Ingredient("Pizza", 5, this.order, this.restaurant));

            }
            if (ingredient.contains("Basil")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Basil", 1.2, this.order, this.restaurant));
            }
            if (ingredient.contains("Bread")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Bread", 0.8, this.order, this.restaurant));
            }
            if (ingredient.contains("Cheese")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Cheese", 1, this.order, this.restaurant));
            }
            if (ingredient.contains("Chicken")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Chicken", 2.4, this.order, this.restaurant));
            }
            if (ingredient.contains("Corn")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Corn", 1, this.order, this.restaurant));
            }
            if (ingredient.contains("Dough")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Dough", 0, this.order, this.restaurant));
            }
            if (ingredient.contains("Egg")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Egg", 1.9, this.order, this.restaurant));
            }
            if (ingredient.contains("Sandwich"))
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Sandwich", 0.4, this.order, this.restaurant));

            if (ingredient.contains("Fries")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Fries", 3.6, this.order, this.restaurant));
            }
            if (ingredient.contains("Garlic")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Garlic", 3.6, this.order, this.restaurant));
            }
            if (ingredient.contains("Hamburger")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Hamburger", 2.8, this.order, this.restaurant));
            }
            if (ingredient.contains("Jalapeno")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Jalapeno", 4, this.order, this.restaurant));
            }
            if (ingredient.contains("Lettuce")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Lettuce", 1.8, this.order, this.restaurant));
            }
            if (ingredient.contains("Mushroom")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Mushroom", 1.6, this.order, this.restaurant));
            }
            if (ingredient.contains("Olive")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Olive", 1.6, this.order, this.restaurant));
            }
            if (ingredient.contains("Onion")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Onion", 3.5, this.order, this.restaurant));
            }
            if (ingredient.contains("Pepper") && !ingredient.contains("Pepperoni")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Pepper", 1.8, this.order, this.restaurant));
            }
            if (ingredient.equals("Pepperoni")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Pepperoni", 3, this.order, this.restaurant));
            }
            if (ingredient.contains("Pickles")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Pickles", 2.8, this.order, this.restaurant));
            }
            if (ingredient.contains("Salami")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Salami", 1.5, this.order, this.restaurant));
            }
            if (ingredient.contains("Sauce")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Sauce", 1, this.order, this.restaurant));
            }
            if (ingredient.contains("Tomato")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Tomato", 3.2, this.order, this.restaurant));
            }
            if (ingredient.contains("Tuna")) {
                for (int i = 0; i < requiredIngredients.get(ingredient); i++)
                    ingredients.add(new Ingredient("Tuna", 2.8, this.order, this.restaurant));
            }
        }
    }

    public double getProfit() {
        for (int i = 0; i < ingredients.size(); i++) {
            profit += ingredients.get(i).getPrice();
        }
        return profit;
    }

}

class Restaurant {
    private double profit;
    private HashMap<String, Integer> mojoodi = new HashMap<>();
    private static Restaurant instance;

    private Restaurant(HashMap<String, Integer> mojoodi) {
        this.mojoodi = mojoodi;
    }

    public static Restaurant getInstance(HashMap<String, Integer> mojoodi) {
        if (instance == null)
            instance = new Restaurant(mojoodi);
        return instance;
    }

    public double getProfit() {
        return this.profit;
    }

    public HashMap<String, Integer> getMojoodi() {
        return this.mojoodi;
    }
}

class Ingredient extends Order {
    private String name;
    private double price;

    public Ingredient(String name, double price, String[] order, Restaurant restaurant) {
        super(order, restaurant);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}

