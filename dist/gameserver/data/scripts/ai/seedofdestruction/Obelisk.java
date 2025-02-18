package ai.seedofdestruction;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage.ScreenMessageAlign;
import l2s.gameserver.utils.Location;

/**
 * @author pchayka
 */
public class Obelisk extends DefaultAI
{
	private static final int[] MOBS = { 22541, 22544, 22543 };
	private boolean _firstTimeAttacked = true;

	public Obelisk(NpcInstance actor)
	{
		super(actor);
		actor.block();
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		_firstTimeAttacked = true;
		NpcInstance actor = getActor();
		actor.broadcastPacket(new ExShowScreenMessage(NpcString.NONE, 3000, ScreenMessageAlign.MIDDLE_CENTER, false, "Obelisk has collapsed. Don't let the enemies jump around wildly anymore!!!"));
		actor.stopDecay();
		for(NpcInstance n : actor.getReflection().getNpcs())
			if(n.getNpcId() == 18777)
				n.stopDamageBlocked();
		super.onEvtDead(killer);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if(_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			for(int i = 0; i < 8; i++)
				for(int mobId : MOBS)
				{
					NpcInstance npc = actor.getReflection().addSpawnWithoutRespawn(mobId, Location.findPointToStay(actor, 400, 1000), 0);
					Creature randomHated = actor.getAggroList().getRandomHated(getMaxHateRange());
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, randomHated != null ? randomHated : attacker, Rnd.get(1, 100));
				}
		}
		super.onEvtAttacked(attacker, damage);
	}
}