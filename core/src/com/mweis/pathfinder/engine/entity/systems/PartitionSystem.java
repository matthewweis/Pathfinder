package com.mweis.pathfinder.engine.entity.systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;
import com.mweis.pathfinder.engine.world.Dungeon;
import com.mweis.pathfinder.engine.world.Room;

/*
 * Because so many Systems demand spatial partitions of entities, and because the operation is so expensive
 * this system will manage all entity partitioning. This is the bridge between the World and the Entity.
 */
public class PartitionSystem extends EntitySystem {
	private Family family = Family.all(CollisionComponent.class, PositionComponent.class).get();
//	private Family posFamily = Family.all(PositionComponent.class).get();
	private Engine engine;
	private Dungeon dungeon;
	
	/*
	 * Maps rooms to all entities within the room.
	 * Note than an entity may be in two rooms at once.
	 */
	private ObjectMap<Room, Array<Entity>> roomToEntityMap;
	private ObjectMap<Entity, Array<Room>> entityToRoomMap;
	
	public PartitionSystem(Dungeon dungeon, Engine engine) {
		super(SystemPriorities.PRE_PROCESSING.get());
		this.dungeon = dungeon;
		this.engine = engine;
		this.roomToEntityMap = new ObjectMap<Room, Array<Entity>>();
		this.entityToRoomMap = new ObjectMap<Entity, Array<Room>>();
	}
	
	@Override
	public void update(float deltaTime) {
		roomToEntityMap.clear();
		entityToRoomMap.clear();
		for (Entity entity : engine.getEntitiesFor(family)) {
			PositionComponent pc = Mappers.positionMapper.get(entity);
			
			if (pc.hasMoved) {
				pc.hasMoved = false;
				Vector2 position = Mappers.positionMapper.get(entity).position;
				Rectangle area = Mappers.collisionMapper.get(entity).getHitBox(position);
				Array<Room> temp = dungeon.getRoomsInArea(area);
				Array<Room> rooms = temp == null ? new Array<Room>(0) : temp;
				
				this.entityToRoomMap.put(entity, rooms);
				
				for (Room room : rooms) {
					if (roomToEntityMap.containsKey(room)) {
						roomToEntityMap.get(room).add(entity);
					} else {
						Array<Entity> list = new Array<Entity>();
						list.add(entity);
						roomToEntityMap.put(room, list);
					}
				}
			}
		}
	}
	
	public Array<Entity> entitiesInRoom(Room room) {
		if (roomToEntityMap.containsKey(room)) {
			return roomToEntityMap.get(room);
		} else {
			return new Array<Entity>(0); // return an empty (BUT NOT NULL) list
		}
	}
	
	public Array<Room> roomsContainingEntity(Entity entity) {
		if (entityToRoomMap.containsKey(entity)) {
			return entityToRoomMap.get(entity);
		} else {
			return new Array<Room>(0); // return an empty (BUT NOT NULL) list
		}
	}
}
