import com.google.gson.Gson;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Field test = new Field(4);
		for(HexCoordinate hex: test.iterateOverHexagons()){
			System.out.println(hex);
		}

		for(Hexagon hexagons: test){
			System.out.println(hexagons);
		}

		Gson gson = new Gson();
		String json = gson.toJson(test);

		System.out.println(json);

		HexCoordinate a = new HexCoordinate(1,0,-1);
		HexCoordinate b = new HexCoordinate(0,2,-2);

		System.out.println(HexCoordinate.subtract(a,b));

		int[] testarr = new int[]{1,6,12};
		System.out.println(test.isInLine(testarr));
	}
}
