using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace FlamingWookie
{
    class Program
    {
        static void Main(string[] args)
        {
            Player player = new Player()
                {
                    Name = "Wookie"
                };

            Console.WriteLine("{0} ist in Location {1}", player.Name, player.Location.Name);
            Console.WriteLine("Und kanns folgendes tun: " + String.Join(", ", player.DiscoverActions().ConvertAll((a) => a.Name )));

            // Der spieler Spricht: "<action name> <action parameters>"
            var say = player.DiscoverActions()[0];
            say.Parameter = "Hallo World";

            player.Location.HandleAction(say);

            Console.ReadKey();
        }
    }

    internal class Player : IActionHandler, IGameObject
    {
        
        public Location Location { get; set; }
        public String Name { get; set; }

        public Player()
        {
            Location = new Location();
            Name = "Unknown Player";
        }

        public void HandleAction(GeneralAction action)
        {
            action.Function(this);
        }

        public List<PlayerAction> DiscoverActions()
        {
            // Iteriere über alles was du kennst und generiere aktionen
            return new List<PlayerAction>()
                {
                    new SpeakAction(this),
                    new PlayerMoveAction(this)
                };
        }

        public void SendMessage(string msg)
        {
            Console.WriteLine("[{0}] hears: {1}", Name, msg);
        }
    }

    internal interface IGameObject
    {
    }

    internal class PlayerMoveAction : PlayerAction
    {
        public PlayerMoveAction(Player player) : base(player)
        {
            Name = "move";

            Function = (scope) =>
                {
                    Location targetLoc = scope as Location;
                    if (targetLoc != null)
                    {
                        if (!player.Location.Contains(player))
                        {
                            player.SendMessage("Da bist du gar nicht!");
                            return;
                        }
                        player.Location.Remove(player);
                        targetLoc.Add(player);
                        player.Location = targetLoc;
                    }
                    else
                    {
                        player.SendMessage("Da kannst du nicht lang!");
                    }
                };
        }
    }

    internal class SpeakAction : PlayerAction
    {
        public SpeakAction(Player player)
            : base(player)
        {
            Name = "say";

            Function = (scope) =>
                {
                    Player target = scope as Player;

                    if (target != null)
                    {
                        Console.WriteLine("{0} says '{1}' to {2}", player.Name, Parameter, target.Name);
                    }
                };
        }
    }


    internal class PlayerAction : GeneralAction
    {
        protected readonly Player player;
        
        public PlayerAction(Player player)
        {
            this.player = player;
        }
    }

    internal class GeneralAction
    {
        public Action<Object> Function { get; set; }
        public String Name { get; set; }
        public string Parameter { get; set; }

        public GeneralAction()
        {
            Name = "unknown action";
        }
    }

    internal class Location : IActionHandler
    {
        public String Name { get; set; }

        private List<IGameObject> content = new List<IGameObject>(); 

        public Location()
        {
            Name = "unknown Location";
        }

        public void HandleAction(GeneralAction action)
        {
            // For each actionHandler in this Location
            IActionHandler handler = new Player()
                {
                    Name ="Another Player"
                };
            handler.HandleAction(action);
        }

        public bool Contains(IGameObject obj)
        {
            return content.Contains(obj);
        }

        public void Remove(IGameObject obj)
        {
            content.Remove(obj);
        }

        public void Add(IGameObject obj)
        {
            content.Add(obj);
        }
    }

    internal interface IActionHandler
    {
        void HandleAction(GeneralAction action);
    }
}
