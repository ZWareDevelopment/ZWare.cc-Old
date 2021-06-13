package dev.zihasz.zware.manager;

import dev.zihasz.zware.relations.Relation;
import dev.zihasz.zware.relations.RelationType;
import dev.zihasz.zware.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RelationManager implements Util {
	private static final List<Relation> relations = new ArrayList<>();

	public RelationManager() {
		relations.add(new Relation(mc.session.getUsername(), mc.session.getPlayerID(), RelationType.FRIEND));
	}

	public static boolean isFriend(UUID uuid) {
		for (Relation relation : relations) {
			if (relation.getType() == RelationType.FRIEND && relation.getUuid().equals(uuid))
				return true;
		}
		return false;
	}
	public static boolean isEnemy(UUID uuid) {
		for (Relation relation : relations) {
			if (relation.getType() == RelationType.ENEMY && relation.getUuid().equals(uuid))
				return true;
		}
		return false;
	}
	public static boolean isAlly(UUID uuid) {
		for (Relation relation : relations) {
			if (relation.getType() == RelationType.ALLY && relation.getUuid().equals(uuid))
				return true;
		}
		return false;
	}

	public static Relation getRelation(UUID uuid) {
		for (Relation relation : relations) {
			if (relation.getType() == RelationType.FRIEND && relation.getUuid().equals(uuid))
				return relation;
		}
		return null;
	}
}
