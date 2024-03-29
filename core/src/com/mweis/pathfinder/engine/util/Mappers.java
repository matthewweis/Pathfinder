package com.mweis.pathfinder.engine.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.mweis.pathfinder.engine.entity.components.*; // all components need to be implemented as all mappings are here.
import com.mweis.pathfinder.engine.entity.components.commands.*; // and all commmands have mappings as well.

public class Mappers {
	public static final ComponentMapper<AIComponent> aiMapper = ComponentMapper.getFor(AIComponent.class);
	public static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
	public static final ComponentMapper<DirectionComponent> directionMapper = ComponentMapper.getFor(DirectionComponent.class);
	public static final ComponentMapper<PathMovementCommand> pathMovementMapper = ComponentMapper.getFor(PathMovementCommand.class);
	public static final ComponentMapper<MovementCommand> movementMapper = ComponentMapper.getFor(MovementCommand.class);
	public static final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<SpeedComponent> speedMapper = ComponentMapper.getFor(SpeedComponent.class);
	public static final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
}
