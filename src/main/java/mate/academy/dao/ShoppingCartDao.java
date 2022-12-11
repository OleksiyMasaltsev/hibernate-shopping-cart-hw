package mate.academy.dao;

import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

import java.util.Optional;

public interface ShoppingCartDao {
    ShoppingCart add(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getByUser(User user);

    void update(ShoppingCart shoppingCart);
}
