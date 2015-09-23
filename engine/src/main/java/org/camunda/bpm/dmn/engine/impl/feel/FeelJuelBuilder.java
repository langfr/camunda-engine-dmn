/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.dmn.engine.impl.feel;

import de.odysseus.el.misc.TypeConverter;
import de.odysseus.el.tree.impl.Builder;
import de.odysseus.el.tree.impl.Parser;
import de.odysseus.el.tree.impl.Scanner;
import de.odysseus.el.tree.impl.ast.AstBinary;
import de.odysseus.el.tree.impl.ast.AstNode;
import de.odysseus.el.tree.impl.ast.AstUnary;

public class FeelJuelBuilder extends Builder {

	private static final long serialVersionUID = 1L;

	static Scanner.ExtensionToken COMMA = new Scanner.ExtensionToken(",");
  static Scanner.ExtensionToken START_RANGE_CLOSED = new Scanner.ExtensionToken("[");
  static Scanner.ExtensionToken RANGE_SEPARATOR = new Scanner.ExtensionToken("..");
  static Scanner.ExtensionToken STOP_RANGE_CLOSED = new Scanner.ExtensionToken("]");

  static Parser.ExtensionHandler COMMA_HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.OR) {
		@Override
		public AstNode createAstNode(AstNode... children) {
			return new AstBinary(children[0], children[1], AstBinary.OR);
		};
	};

  static AstUnary.Operator START_RANGE_CLOSED_OPERATOR = new AstUnary.SimpleOperator() {
    protected Object apply(TypeConverter converter, Object o) {
      return true;
    }
  };

  static AstBinary.Operator RANGE_SEPARATOR_OPERATOR = new AstBinary.SimpleOperator() {
    protected Object apply(TypeConverter converter, Object o1, Object o2) {
      return true;
    }
  };

  static AstUnary.Operator STOP_RANGE_CLOSED_OPERATOR = new AstUnary.SimpleOperator() {
    protected Object apply(TypeConverter converter, Object o) {
      return true;
    }
  };

  static Parser.ExtensionHandler START_RANGE_CLOSED_HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.UNARY) {
    public AstNode createAstNode(AstNode... children) {
      return new AstUnary(children[0], START_RANGE_CLOSED_OPERATOR);
    }
  };

  static Parser.ExtensionHandler RANGE_SEPARATOR_HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.AND) {
    public AstNode createAstNode(AstNode... children) {
      return new AstBinary(children[0], children[1], RANGE_SEPARATOR_OPERATOR);
    }
  };

  static Parser.ExtensionHandler STOP_RANGE_CLOSED_HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.UNARY) {
    public AstNode createAstNode(AstNode... children) {
      return new AstUnary(children[0], STOP_RANGE_CLOSED_OPERATOR);
    }
  };

	static class ExtendedParser extends Parser {
		public ExtendedParser(Builder context, String input) {
			super(context, input);
			putExtensionHandler(COMMA, COMMA_HANDLER);
      putExtensionHandler(START_RANGE_CLOSED, START_RANGE_CLOSED_HANDLER);
      putExtensionHandler(RANGE_SEPARATOR, RANGE_SEPARATOR_HANDLER);
      putExtensionHandler(STOP_RANGE_CLOSED, STOP_RANGE_CLOSED_HANDLER);
		}

		@Override
		protected Scanner createScanner(String expression) {
			return new Scanner(expression) {
				@Override
				protected Token nextEval() throws ScanException {
          String input = getInput();
          int position = getPosition();
          char charAt = input.charAt(position);
          switch (charAt) {
            case ',':
              return COMMA;
            case '[':
              return START_RANGE_CLOSED;
            case ']':
              return STOP_RANGE_CLOSED;
            case '.':
              if (position > 0 && input.charAt(position - 1) == '.') {
                return RANGE_SEPARATOR;
              }
            default:
              return super.nextEval();
          }
				}
			};
		}
	}

	public FeelJuelBuilder() {
		super();
	}

	public FeelJuelBuilder(Feature... features) {
		super(features);
	}

	@Override
	protected Parser createParser(String expression) {
		return new ExtendedParser(this, expression);
	}

}
