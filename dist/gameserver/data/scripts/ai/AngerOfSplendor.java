package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.data.xml.holder.NpcHolder;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.StatusUpdatePacket;


public class AngerOfSplendor extends Fighter
{

    public AngerOfSplendor(NpcInstance actor)
    {
        super(actor);
        AI_TASK_ATTACK_DELAY = 1000;
        AI_TASK_ACTIVE_DELAY = 1000;
    }

    protected void onEvtAttacked(Creature attacker, int damage)
    {
        NpcInstance actor = getActor();
		if(attacker == null || actor.isDead())
			return;
			
			int transformer = 21528;
			if(transformer > 0)
		{
			int chance = actor.getParameter("transformChance", 90);
			if(chance == 100 || ((MonsterInstance) actor).getChampion() == 0 && actor.getCurrentHpPercents() > 50 && Rnd.chance(chance))
			{
				MonsterInstance npc = (MonsterInstance) NpcHolder.getInstance().getTemplate(transformer).getNewInstance();
				npc.setSpawnedLoc(actor.getLoc());
				npc.setReflection(actor.getReflection());
				npc.setChampion(((MonsterInstance) actor).getChampion());
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
				npc.spawnMe(npc.getSpawnedLoc());
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
				actor.doDie(actor);
				actor.decayMe();
				attacker.setTarget(npc);
				attacker.sendPacket(npc.makeStatusUpdate(StatusUpdatePacket.CUR_HP, StatusUpdatePacket.MAX_HP));
				return;
			}
        }
        super.onEvtAttacked(attacker, damage);
    }
}