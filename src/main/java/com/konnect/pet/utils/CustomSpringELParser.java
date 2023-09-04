package com.konnect.pet.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringELParser {
	private CustomSpringELParser() {
	}

	/**
	 * 주어진 SpEL 문자열을 파싱하고 평가한다. 제공된 매개 변수 이름과 인수를 사용한다.
	 *
	 * @param parameterNames SpEL 문자열 내의 매개 변수 이름들.
	 * @param args           매개 변수의 값들.
	 * @param key            평가될 SpEL 문자열.
	 * @return SpEL 문자열 평가의 결과.
	 */
	public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 제공된 인수를 해당 매개 변수 이름에 할당
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}

		// SpEL 문자열 파싱 및 평가
		return parser.parseExpression(key).getValue(context, Object.class);
	}
}
