package quests;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.QuestState;

/**
 * @author pchayka
 *         Daily quest
 *         ВНИМАНИЕ! Данный квест можно выполнять не только группой, но и командным каналом, все персонажи в командном канале имеют шанс получить квестовые предметы. После убийства боссов будут появляться специальные НПЦ - мертвые тела боссов, для получения квестовых предметов необходимо будет "поговорить" с этим НПЦ.
 */
public class _456_DontKnowDontCare extends QuestScript
{
	private static final int[] SeparatedSoul = {32864, 32865, 32866, 32867, 32868, 32869, 32870};
	private static final int DrakeLordsEssence = 17251;
	private static final int BehemothLeadersEssence = 17252;
	private static final int DragonBeastsEssence = 17253;
	//private static final int DrakeLord = 25725;
	//private static final int BehemothLeader = 25726;
	//private static final int DragonBeast = 25727;

	private static final int DrakeLordCorpse = 32884;
	private static final int BehemothLeaderCorpse = 32885;
	private static final int DragonBeastCorpse = 32886;

	//Reward set
	private static final int[] weapons = {15558, 15559, 15560, 15561, 15562, 15563, 15564, 15565, 15566, 15567, 15568, 15569, 15570, 15571};
	private static final int[] armors = {15743, 15744, 15745, 15746, 15747, 15748, 15749, 15750, 15751, 15752, 15753, 15754, 15755, 15756, 15757, 15759, 15758};
	private static final int[] accessory = {15763, 15764, 15765};
	private static final int[] scrolls = {6577, 6578, 959};
	private static final int[] reward_attr_crystal = {9553, 9552, 9555, 9554, 9556, 9557};
	private static final int gemstone_s = 2134;


	public _456_DontKnowDontCare()
	{
		super(PARTY_ALL, DAILY);
		addStartNpc(SeparatedSoul);
		addTalkId(DrakeLordCorpse, BehemothLeaderCorpse, DragonBeastCorpse);
		addQuestItem(DrakeLordsEssence, BehemothLeadersEssence, DragonBeastsEssence);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("sepsoul_q456_05.htm"))
		{
			st.setCond(1);
		}
		else if(event.equalsIgnoreCase("take_essense"))
		{
			if(st.getCond() == 1)
			{
				if(npc == null || st.get("RaidKilled") == null)
					return null;
				switch(npc.getNpcId())
				{
					case DrakeLordCorpse:
						if(st.getQuestItemsCount(DrakeLordsEssence) < 1)
							st.giveItems(DrakeLordsEssence, 1, false, false);
						break;
					case BehemothLeaderCorpse:
						if(st.getQuestItemsCount(BehemothLeadersEssence) < 1)
							st.giveItems(BehemothLeadersEssence, 1, false, false);
						break;
					case DragonBeastCorpse:
						if(st.getQuestItemsCount(DragonBeastsEssence) < 1)
							st.giveItems(DragonBeastsEssence, 1, false, false);
						break;
					default:
						break;
				}
				st.unset("RaidKilled");
				if(st.getQuestItemsCount(DrakeLordsEssence) > 0 && st.getQuestItemsCount(BehemothLeadersEssence) > 0 && st.getQuestItemsCount(DragonBeastsEssence) > 0)
					st.setCond(2);
			}
			return null;
		}
		else if(event.equalsIgnoreCase("sepsoul_q456_08.htm"))
		{
			st.takeAllItems(DrakeLordsEssence);
			st.takeAllItems(BehemothLeadersEssence);
			st.takeAllItems(DragonBeastsEssence);

			if(Rnd.chance(1))
				st.giveItems(weapons[Rnd.get(weapons.length)], 1, false, false);
			else if(Rnd.chance(5))
				st.giveItems(armors[Rnd.get(armors.length)], 1, false, false);
			else if(Rnd.chance(10))
				st.giveItems(accessory[Rnd.get(accessory.length)], 1, false, false);
			else if(Rnd.chance(15))
				st.giveItems(scrolls[Rnd.get(scrolls.length)], 1, false, false);
			else 
				st.giveItems(reward_attr_crystal[Rnd.get(reward_attr_crystal.length)], 1, false, false);
   
			st.giveItems(gemstone_s, 3, false, false);
			st.finishQuest();
		}

		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = NO_QUEST_DIALOG;
		int cond = st.getCond();
		if(ArrayUtils.contains(SeparatedSoul, npc.getNpcId()))
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 80)
					htmltext = "sepsoul_q456_01.htm";
				else
					htmltext = "sepsoul_q456_00.htm";
			}
			else if(cond == 1)
				htmltext = "sepsoul_q456_06.htm";
			else if(cond == 2)
				htmltext = "sepsoul_q456_07.htm";
		}

		return htmltext;
	}

	@Override
	public String onCompleted(NpcInstance npc, QuestState st)
	{
		String htmltext = COMPLETED_DIALOG;
		if(ArrayUtils.contains(SeparatedSoul, npc.getNpcId()))
			htmltext = "sepsoul_q456_00a.htm";
		return htmltext;
	}
}