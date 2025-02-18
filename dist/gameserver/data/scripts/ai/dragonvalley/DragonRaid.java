package ai.dragonvalley;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;

/**
 * @author pchayka
 */
public class DragonRaid extends Fighter
{
	
	private long _lastHit;
	
	public DragonRaid(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(_lastHit + 1500000 < System.currentTimeMillis())
		{
			actor.deleteMe();
			return false;
		}
		return super.thinkActive();
	}

	@Override
	protected void onEvtSpawn()
	{
		_lastHit = System.currentTimeMillis();
		super.onEvtSpawn();
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		_lastHit = System.currentTimeMillis();

		super.onEvtAttacked(attacker, damage);
	}
	
	
}