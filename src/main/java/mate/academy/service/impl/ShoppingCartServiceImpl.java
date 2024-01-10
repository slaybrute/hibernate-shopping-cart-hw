package mate.academy.service.impl;

import java.util.Collections;
import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) throws EntityNotFoundException {
        ShoppingCart shoppingCart = shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find shopping cart by user: " + user));
        Ticket ticket = new Ticket(movieSession, user);
        ticketDao.add(ticket);
        List<Ticket> tickets = shoppingCart.getTickets();
        tickets.add(ticket);
        shoppingCart.setTickets(tickets);
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) throws EntityNotFoundException {
        return shoppingCartDao.getByUser(user).orElseThrow(() ->
                new EntityNotFoundException("Cannot find shopping cart by user: " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart(null, user);
        shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(Collections.emptyList());
        shoppingCartDao.update(shoppingCart);
    }

    @Override
    public String toString() {
        return "ShoppingCartServiceImpl{"
                + "shoppingCartDao=" + shoppingCartDao
                + ", ticketDao=" + ticketDao + '}';
    }
}
