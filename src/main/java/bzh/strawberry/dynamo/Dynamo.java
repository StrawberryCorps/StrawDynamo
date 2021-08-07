package bzh.strawberry.dynamo;

import bzh.strawberry.dynamo.client.DynamoClient;
import bzh.strawberry.dynamo.command.*;
import bzh.strawberry.dynamo.configuration.ConfigLoader;
import bzh.strawberry.dynamo.configuration.DynamoConfig;
import bzh.strawberry.dynamo.data.Players;
import bzh.strawberry.dynamo.data.Status;
import bzh.strawberry.dynamo.data.Version;
import bzh.strawberry.dynamo.database.JedisManager;
import bzh.strawberry.dynamo.logger.DynamoLogger;
import bzh.strawberry.dynamo.network.initializer.DynamoChannelInitializer;
import bzh.strawberry.dynamo.strategy.BalancingStrategy;
import bzh.strawberry.dynamo.strategy.RoundRobinBalancingStrategy;
import bzh.strawberry.dynamo.task.ProxyCheck;
import bzh.strawberry.dynamo.utils.EncodeUtils;
import bzh.strawberry.dynamo.utils.ProxyData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import org.fusesource.jansi.AnsiConsole;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Crée par Eclixal
 * Le 09/05/2018.
 */
public class Dynamo implements Runnable {

    private static Dynamo dynamo;
    private static Logger logger;
    private static ConsoleReader consoleReader;

    private boolean launch;
    private String host;
    private int port;
    private BalancingStrategy balancingStrategy;
    private Status status;
    private JedisManager jedisManager;

    private DynamoConfig dynamoConfig;

    private String statusMessage;

    private List<ProxyData> proxyDatas = new ArrayList<>();
    private List<DynamoClient> dynamoClients = new ArrayList<>();

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    private CommandManager commandManager = new CommandManager();

    public Dynamo() {
        dynamo = this;
        launch = true;
        try {
            consoleReader = new ConsoleReader();
            consoleReader.setExpandEvents(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        AnsiConsole.systemInstall();
        LogManager.getLogManager().reset();

        logger = new DynamoLogger(consoleReader);

        commandManager.addCommand(new EndCommand("end", new String[] {"stop", "kill"}, "Arrete Dynamo :D"));
        commandManager.addCommand(new KickAllCommand("kickall", new String [] {}, "Kick tous les joueurs connecté sur Dynamo"));
        commandManager.addCommand(new StatsCommand("stats", new String[]{"info", "infos"}, "Statistique sur le trafic de Dynamo !"));
        commandManager.addCommand(new HelpCommand("help", new String[]{"aide"}, "Information sur les commandes"));

        List<String> cmds = new ArrayList<>();
        commandManager.getCommands().forEach(c -> cmds.add(c.getName()));
        consoleReader.addCompleter(new StringsCompleter(cmds));

        //jedisManager = new JedisManager("127.0.0.1", 1200, "Bk5a6r5ZhT33D99frQK34h34D3uqfMyQ", null);

        try {
            this.dynamoConfig = ConfigLoader.loadConfig("config.json", DynamoConfig.class);
            this.host = dynamoConfig.getHost();
            this.port = dynamoConfig.getPort();
            this.proxyDatas = dynamoConfig.getProxyDatas();
        } catch (Exception err) {
            err.printStackTrace();
            System.exit(1);
        }

        balancingStrategy = new RoundRobinBalancingStrategy(proxyDatas);

        new Timer("ProxyCheck").scheduleAtFixedRate(new ProxyCheck(balancingStrategy), 0, TimeUnit.SECONDS.toMillis(1));

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(dynamoConfig.getThreads());

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, dynamoConfig.getBacklog())
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.AUTO_READ, false)
                    .childOption(ChannelOption.SO_TIMEOUT, 30000)
                    .childHandler(new DynamoChannelInitializer());

            ChannelFuture f = bootstrap.bind(dynamoConfig.getPort()).sync();

            try {
                String faviconString = EncodeUtils.encodeToString(ImageIO.read(new File("server-icon.png")), "png");
                status = new Status("&6&lEclixal&r &6c'est vraiment un DIEU !"/*jedisManager.getRessource().get("dynamo:motd")*/, new Players("200"), new Version("Dynamo", "%protocolUser%"));
                statusMessage = "{\"version\":{\"name\":\"" + status.getVersion().getName() + "\",\"protocol\":" + status.getVersion().getProtocol() + "},\"players\":{\"max\":" + status.getPlayers().getMax() + ",\"online\":" + status.getPlayers().getOnline() + "},\"description\":{\"text\":\"" + status.getDescription() + "\"},\"favicon\":\"data:image/png;base64," + faviconString + "\"}";
            } catch (Exception e) {
                e.printStackTrace();
            }

            f.channel().closeFuture().sync();

            launch = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            launch = false;
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        launch();
    }

    public boolean isLaunch() {
        return launch;
    }

    public static Dynamo getDynamo() {
        return dynamo;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public BalancingStrategy getBalancingStrategy() {
        return balancingStrategy;
    }

    public synchronized void addClient(DynamoClient dynamoClient) {
        dynamoClients.add(dynamoClient);
    }

    public synchronized void removeClient(DynamoClient dynamoClient) {
        dynamoClients.remove(dynamoClient);
    }

    public int getClientsOnline() {
        return dynamoClients.size();
    }

    public List<DynamoClient> getDynamoClients() {
        return dynamoClients;
    }

    public void kill() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        //jedisManager.close();
        System.exit(0);
    }

    public JedisManager getJedisManager() {
        return jedisManager;
    }
}