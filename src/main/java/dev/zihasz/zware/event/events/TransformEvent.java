package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public abstract class TransformEvent extends Event {

	private final EnumHandSide handSide;

	public TransformEvent(EventState state, final EnumHandSide handSide) {
		super(state);
		this.handSide = handSide;
	}

	public EnumHandSide getHandSide() { return handSide; }

	public static class FirstPerson extends TransformEvent {

		public FirstPerson(EventState state, final EnumHandSide handSide) {
			super(state, handSide);
		}

		public static class Side extends FirstPerson {
			public Side(EventState state, final EnumHandSide handSide) {
				super(state, handSide);
			}
		}
		public static class Eat extends FirstPerson {

			private final ItemStack itemStack;

			public Eat(EventState state, final EnumHandSide handSide, final ItemStack itemStack) {
				super(state, handSide);
				this.itemStack = itemStack;
			}

			public ItemStack getItemStack() { return itemStack; }

		}

	}

}
