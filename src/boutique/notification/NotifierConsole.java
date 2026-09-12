package boutique.notification;

public final class NotifierConsole implements Notifier {

    @Override
    public void notifier(String clientId, int totalCents) {
        System.out.println("Notification : commande validée pour " + clientId
                + " — total de " + totalCents + " centimes.");
    }
}